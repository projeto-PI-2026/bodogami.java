package com.nexatech.bodogami.service;

import com.nexatech.bodogami.entity.Exemplar;
import com.nexatech.bodogami.entity.Genero;
import com.nexatech.bodogami.entity.Jogo;
import com.nexatech.bodogami.entity.TipoJogo;
import com.nexatech.bodogami.exception.ExemplarNaoEncontradoException;
import com.nexatech.bodogami.exception.JogoInvalidoException;
import com.nexatech.bodogami.exception.JogoNaoEncontradoException;
import com.nexatech.bodogami.repository.ExemplarRepository;
import com.nexatech.bodogami.repository.GeneroRepository;
import com.nexatech.bodogami.repository.JogoRepository;
import com.nexatech.bodogami.repository.TipoJogoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;
    private final TipoJogoRepository tipoJogoRepository;
    private final GeneroRepository generoRepository;
    private final ExemplarRepository exemplarRepository;

    public JogoService(
            JogoRepository jogoRepository,
            TipoJogoRepository tipoJogoRepository,
            GeneroRepository generoRepository,
            ExemplarRepository exemplarRepository
    ) {
        this.jogoRepository = jogoRepository;
        this.tipoJogoRepository = tipoJogoRepository;
        this.generoRepository = generoRepository;
        this.exemplarRepository = exemplarRepository;
    }

    public List<Jogo> listar() {
        List<Jogo> jogos = jogoRepository.findAll();
        preencherQuantidade(jogos);
        return jogos;
    }

    public List<Jogo> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new JogoInvalidoException("Informe o nome do jogo para a busca");
        }

        List<Jogo> jogos = jogoRepository.findByNomeContainingIgnoreCase(nome);
        preencherQuantidade(jogos);
        return jogos;
    }

    public Jogo buscarPorId(Integer id) {
        Jogo jogo = jogoRepository.findById(id).orElse(null);
        if (jogo == null) {
            throw new JogoNaoEncontradoException("Jogo não encontrado");
        }

        jogo.setQuantidade((int) exemplarRepository.countByJogoId(id));
        return jogo;
    }

    public List<TipoJogo> listarTipos() {
        return tipoJogoRepository.findAll();
    }

    public List<Genero> listarGeneros() {
        return generoRepository.findAll();
    }

    public Jogo criar(Jogo jogo) {
        validarJogadores(jogo);

        jogo.setTipoJogo(buscarTipoJogo(jogo.getTipoJogo()));
        jogo.setGeneros(buscarGeneros(jogo.getGeneros()));

        return jogoRepository.save(jogo);
    }

    public Jogo atualizar(Integer id, Jogo jogo) {
        Jogo jogoExistente = buscarPorId(id);
        validarJogadores(jogo);

        jogoExistente.setNome(jogo.getNome());
        jogoExistente.setDescricao(jogo.getDescricao());
        jogoExistente.setEditora(jogo.getEditora());
        jogoExistente.setValorAluguelDiaria(jogo.getValorAluguelDiaria());
        jogoExistente.setImagemUrl(jogo.getImagemUrl());
        jogoExistente.setMinJogadores(jogo.getMinJogadores());
        jogoExistente.setMaxJogadores(jogo.getMaxJogadores());
        jogoExistente.setIdadeMin(jogo.getIdadeMin());
        jogoExistente.setTipoJogo(buscarTipoJogo(jogo.getTipoJogo()));

        if (jogo.getGeneros() != null && !jogo.getGeneros().isEmpty()) {
            jogoExistente.setGeneros(buscarGeneros(jogo.getGeneros()));
        }

        return jogoRepository.save(jogoExistente);
    }

    public void deletar(Integer id) {
        Jogo jogo = buscarPorId(id);
        jogoRepository.delete(jogo);
    }

    public void removerExemplar(Integer idJogo) {
        buscarPorId(idJogo);

        Exemplar exemplar = exemplarRepository.findFirstByJogoId(idJogo);
        if (exemplar == null) {
            throw new ExemplarNaoEncontradoException("Exemplar não encontrado para o jogo informado");
        }

        exemplarRepository.delete(exemplar);
    }

    private void preencherQuantidade(List<Jogo> jogos) {
        for (Jogo jogo : jogos) {
            jogo.setQuantidade((int) exemplarRepository.countByJogoId(jogo.getId()));
        }
    }

    private void validarJogadores(Jogo jogo) {
        if (jogo.getMinJogadores() > jogo.getMaxJogadores()) {
            throw new JogoInvalidoException("A quantidade mínima de jogadores não pode ser maior que a máxima");
        }
    }

    private TipoJogo buscarTipoJogo(TipoJogo tipoJogo) {
        TipoJogo tipoJogoEncontrado = tipoJogoRepository.findById(tipoJogo.getId()).orElse(null);
        if (tipoJogoEncontrado == null) {
            throw new JogoInvalidoException("Tipo de jogo não encontrado");
        }

        return tipoJogoEncontrado;
    }

    private List<Genero> buscarGeneros(List<Genero> generos) {
        List<Genero> generosEncontrados = new ArrayList<>();

        for (Genero genero : generos) {
            Genero generoEncontrado = generoRepository.findById(genero.getId()).orElse(null);
            if (generoEncontrado == null) {
                throw new JogoInvalidoException("Gênero não encontrado");
            }

            generosEncontrados.add(generoEncontrado);
        }

        return generosEncontrados;
    }
}
