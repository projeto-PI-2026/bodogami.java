package com.nexatech.bodogami.service;

import com.nexatech.bodogami.dto.AlugueisClienteResponseDto;
import com.nexatech.bodogami.dto.HistoricoEventoDto;
import com.nexatech.bodogami.dto.InadimplenteResponseDto;
import com.nexatech.bodogami.dto.PendenciasResponseDto;
import com.nexatech.bodogami.dto.QuitacaoRequestDto;
import com.nexatech.bodogami.dto.QuitacaoResponseDto;
import com.nexatech.bodogami.exception.NegocioException;
import com.nexatech.bodogami.mapper.ClienteFinanceiroMapper;
import com.nexatech.bodogami.entity.Aluguel;
import com.nexatech.bodogami.entity.Multa;
import com.nexatech.bodogami.repository.ClienteFinanceiroRepository;
import com.nexatech.bodogami.strategy.QuitacaoStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClienteFinanceiroService {

    public static final String SITUACAO_INADIMPLENTE = "INADIMPLENTE";
    public static final String SITUACAO_EM_DIA = "EM_DIA";
    public static final String STATUS_MULTA_PENDENTE = "PENDENTE";
    public static final String STATUS_MULTA_PAGA = "PAGA";
    public static final String TIPO_PAGAMENTO_MULTA = "QUITACAO_MULTA";

    private final ClienteFinanceiroRepository repository;
    private final List<QuitacaoStrategy> estrategiasQuitacao;

    public ClienteFinanceiroService(ClienteFinanceiroRepository repository,
                                    List<QuitacaoStrategy> estrategiasQuitacao) {
        this.repository = repository;
        this.estrategiasQuitacao = estrategiasQuitacao;
    }

    public PendenciasResponseDto consultarPendencias(Integer idCliente) {
        String nome = validarCliente(idCliente);

        List<Multa> multas = repository.listarMultasPendentes(idCliente);

        PendenciasResponseDto resposta = new PendenciasResponseDto();
        resposta.setIdCliente(idCliente);
        resposta.setNomeCliente(nome);
        resposta.setSituacaoFinanceira(definirSituacao(multas));
        resposta.setQuantidadePendencias(multas.size());
        resposta.setValorTotalEmAberto(somarSaldo(multas));
        resposta.setPendencias(ClienteFinanceiroMapper.toPendenciaDto(multas));
        return resposta;
    }

    public AlugueisClienteResponseDto listarAlugueis(Integer idCliente) {
        validarCliente(idCliente);

        List<Aluguel> alugueis = repository.listarAlugueis(idCliente);

        int totalJogos = 0;
        for (Aluguel aluguel : alugueis) {
            totalJogos += aluguel.getQuantidadeJogos();
        }

        AlugueisClienteResponseDto resposta = new AlugueisClienteResponseDto();
        resposta.setIdCliente(idCliente);
        resposta.setQuantidadeAlugueis(alugueis.size());
        resposta.setQuantidadeJogos(totalJogos);
        resposta.setAlugueis(ClienteFinanceiroMapper.toAluguelDto(alugueis));
        return resposta;
    }

    public List<InadimplenteResponseDto> listarInadimplentes() {
        List<InadimplenteResponseDto> inadimplentes = repository.listarInadimplentes();
        for (InadimplenteResponseDto inadimplente : inadimplentes) {
            inadimplente.setSituacaoFinanceira(SITUACAO_INADIMPLENTE);
        }
        return inadimplentes;
    }

    public List<HistoricoEventoDto> consultarHistorico(Integer idCliente) {
        validarCliente(idCliente);
        return repository.listarHistorico(idCliente);
    }

    // Também serve ao módulo de aluguéis para bloquear novos aluguéis (RN-04).
    public boolean clienteInadimplente(Integer idCliente) {
        validarCliente(idCliente);
        return !repository.listarMultasPendentes(idCliente).isEmpty();
    }

    @Transactional
    public QuitacaoResponseDto quitarPendencia(Integer idCliente, QuitacaoRequestDto dto) {
        validarCliente(idCliente);

        if (!repository.usuarioAtivo(dto.getIdUsuario())) {
            throw new NegocioException(400, "Usuário responsável inexistente ou inativo");
        }

        QuitacaoStrategy estrategia = escolherEstrategia(dto.getModalidade());
        validarIdsSemDuplicidade(dto.getIdsMultas());

        List<Multa> multas = repository.buscarMultasPorIds(dto.getIdsMultas());
        validarMultas(idCliente, dto.getIdsMultas(), multas);

        BigDecimal valorRecebido = dto.getValor();
        if (valorRecebido.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegocioException(400, "O valor recebido deve ser maior que zero");
        }

        BigDecimal totalDevido = somarSaldo(multas);
        List<BigDecimal> aplicacoes = estrategia.distribuir(multas, totalDevido, valorRecebido);

        LocalDateTime agora = LocalDateTime.now();
        Integer idPagamento = repository.inserirPagamento(idCliente, dto.getIdUsuario(), TIPO_PAGAMENTO_MULTA,
                valorRecebido, dto.getFormaPagamento(), dto.getNumeroComprovante(), agora);

        int quitadas = 0;
        int parciais = 0;
        for (int i = 0; i < multas.size(); i++) {
            Multa multa = multas.get(i);
            BigDecimal aplicado = aplicacoes.get(i);

            if (aplicado.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal novoValorPago = multa.getValorPago().add(aplicado);
                String novoStatus = STATUS_MULTA_PENDENTE;
                if (novoValorPago.compareTo(multa.getValor()) >= 0) {
                    novoStatus = STATUS_MULTA_PAGA;
                    quitadas++;
                } else {
                    parciais++;
                }

                int linhas = repository.atualizarMulta(multa.getId(), multa.getValorPago(), novoValorPago, novoStatus);
                if (linhas != 1) {
                    throw new NegocioException(409,
                            "A multa " + multa.getId() + " foi alterada por outra operação. Tente novamente.");
                }
                repository.inserirPagamentoMulta(idPagamento, multa.getId(), aplicado);
            }
        }

        List<Multa> restantes = repository.listarMultasPendentes(idCliente);

        QuitacaoResponseDto resposta = new QuitacaoResponseDto();
        resposta.setIdPagamento(idPagamento);
        resposta.setIdCliente(idCliente);
        resposta.setModalidade(estrategia.getModalidade());
        resposta.setFormaPagamento(dto.getFormaPagamento());
        resposta.setValorPago(valorRecebido);
        resposta.setDataPagamento(agora);
        resposta.setMultasQuitadas(quitadas);
        resposta.setMultasParcialmentePagas(parciais);
        resposta.setValorEmAbertoRestante(somarSaldo(restantes));
        resposta.setSituacaoFinanceira(definirSituacao(restantes));
        return resposta;
    }

    private String validarCliente(Integer idCliente) {
        if (idCliente == null || idCliente <= 0) {
            throw new NegocioException(400, "Identificador de cliente inválido");
        }
        String nome = repository.buscarNomeCliente(idCliente);
        if (nome == null) {
            throw new NegocioException(404, "Cliente não encontrado");
        }
        return nome;
    }

    private QuitacaoStrategy escolherEstrategia(String modalidade) {
        for (QuitacaoStrategy estrategia : estrategiasQuitacao) {
            if (estrategia.getModalidade().equalsIgnoreCase(modalidade)) {
                return estrategia;
            }
        }
        throw new NegocioException(400, "Modalidade de quitação inválida: " + modalidade);
    }

    private void validarIdsSemDuplicidade(List<Integer> ids) {
        Set<Integer> vistos = new HashSet<>();
        for (Integer id : ids) {
            if (id == null || !vistos.add(id)) {
                throw new NegocioException(400, "A lista de multas contém valores nulos ou repetidos");
            }
        }
    }

    private void validarMultas(Integer idCliente, List<Integer> ids, List<Multa> multas) {
        if (multas.size() != ids.size()) {
            throw new NegocioException(404, "Multa não encontrada para este cliente");
        }
        for (Multa multa : multas) {
            if (!idCliente.equals(multa.getIdCliente())) {
                throw new NegocioException(404, "Multa não encontrada para este cliente");
            }
            if (!STATUS_MULTA_PENDENTE.equals(multa.getStatus())) {
                throw new NegocioException(409, "A multa " + multa.getId() + " já está paga");
            }
        }
    }

    private BigDecimal somarSaldo(List<Multa> multas) {
        BigDecimal total = BigDecimal.ZERO;
        for (Multa multa : multas) {
            total = total.add(multa.getSaldo());
        }
        return total;
    }

    private String definirSituacao(List<Multa> multasPendentes) {
        if (multasPendentes.isEmpty()) {
            return SITUACAO_EM_DIA;
        }
        return SITUACAO_INADIMPLENTE;
    }
}