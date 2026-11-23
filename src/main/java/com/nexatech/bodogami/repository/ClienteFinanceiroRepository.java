package com.nexatech.bodogami.repository;

import com.nexatech.bodogami.dto.HistoricoEventoDto;
import com.nexatech.bodogami.dto.InadimplenteResponseDto;
import com.nexatech.bodogami.entity.Aluguel;
import com.nexatech.bodogami.entity.Multa;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ClienteFinanceiroRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClienteFinanceiroRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean clienteExiste(Integer idCliente) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM cliente WHERE id_cliente = ?", Integer.class, idCliente);
        return total != null && total > 0;
    }

    public String buscarNomeCliente(Integer idCliente) {
        List<String> nomes = jdbcTemplate.queryForList(
                "SELECT COALESCE(nome, '') FROM cliente WHERE id_cliente = ?", String.class, idCliente);
        if (nomes.isEmpty()) {
            return null;
        }
        return nomes.get(0);
    }

    public boolean usuarioAtivo(Integer idUsuario) {
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE id_usuario = ? AND ativo = TRUE", Integer.class, idUsuario);
        return total != null && total > 0;
    }

    public List<Multa> listarMultasPendentes(Integer idCliente) {
        String sql = "SELECT m.id_multa AS id, m.fk_cliente AS id_cliente, m.fk_aluguel AS id_aluguel, " +
                "a.codigo_retirada AS codigo_retirada, m.tipo, m.valor, m.valor_pago, m.data_vencimento, m.status " +
                "FROM multa m LEFT JOIN aluguel a ON a.id_aluguel = m.fk_aluguel " +
                "WHERE m.fk_cliente = ? AND m.status = 'PENDENTE' " +
                "ORDER BY m.data_vencimento, m.id_multa";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Multa.class), idCliente);
    }

    public List<Multa> buscarMultasPorIds(List<Integer> ids) {
        StringBuilder marcadores = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                marcadores.append(", ");
            }
            marcadores.append("?");
        }

        String sql = "SELECT id_multa AS id, fk_cliente AS id_cliente, fk_aluguel AS id_aluguel, " +
                "tipo, valor, valor_pago, data_vencimento, status " +
                "FROM multa WHERE id_multa IN (" + marcadores + ") " +
                "ORDER BY data_vencimento, id_multa";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Multa.class), ids.toArray());
    }

    public List<Aluguel> listarAlugueis(Integer idCliente) {
        String sql = "SELECT a.id_aluguel AS id, a.codigo_retirada, a.data_agendamento, a.data_retirada, " +
                "a.data_prevista_devolucao, a.data_devolucao, a.valor_total, a.status, " +
                "(SELECT COUNT(*) FROM item_aluguel i WHERE i.fk_aluguel = a.id_aluguel) AS quantidade_jogos " +
                "FROM aluguel a WHERE a.fk_cliente = ? " +
                "ORDER BY COALESCE(a.data_retirada, a.data_agendamento) DESC, a.id_aluguel DESC";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Aluguel.class), idCliente);
    }

    public List<InadimplenteResponseDto> listarInadimplentes() {
        String sql = "SELECT c.id_cliente AS id, c.nome, c.cpf, c.email, c.telefone, c.cidade, c.bairro, " +
                "(SELECT p.nome FROM assinatura s JOIN plano_assinatura p ON p.id_plano = s.fk_plano " +
                "WHERE s.fk_cliente = c.id_cliente AND s.status = 'ATIVA' " +
                "ORDER BY s.data_inicio DESC LIMIT 1) AS plano, " +
                "(SELECT COUNT(*) FROM aluguel a WHERE a.fk_cliente = c.id_cliente) AS quantidade_alugueis, " +
                "COUNT(m.id_multa) AS quantidade_pendencias, " +
                "SUM(m.valor - m.valor_pago) AS valor_em_aberto " +
                "FROM cliente c JOIN multa m ON m.fk_cliente = c.id_cliente AND m.status = 'PENDENTE' " +
                "GROUP BY c.id_cliente, c.nome, c.cpf, c.email, c.telefone, c.cidade, c.bairro " +
                "ORDER BY valor_em_aberto DESC, c.nome";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(InadimplenteResponseDto.class));
    }

    public List<HistoricoEventoDto> listarHistorico(Integer idCliente) {
        String sql = "SELECT * FROM (" +
                "SELECT 'ALUGUEL' AS tipo_evento, a.id_aluguel AS id_referencia, " +
                "COALESCE(a.data_retirada, a.data_agendamento) AS data_evento, " +
                "CONCAT('Aluguel ', a.codigo_retirada, ' - jogos: ', COALESCE((SELECT STRING_AGG(j.nome, ', ') " +
                "FROM item_aluguel i JOIN exemplar e ON e.id_exemplar = i.fk_exemplar " +
                "JOIN jogo j ON j.id_jogo = e.fk_jogo WHERE i.fk_aluguel = a.id_aluguel), 'nenhum')) AS descricao, " +
                "a.valor_total AS valor, a.status AS status " +
                "FROM aluguel a WHERE a.fk_cliente = ? " +
                "UNION ALL " +
                "SELECT 'MULTA', m.id_multa, m.data_criacao, " +
                "CONCAT('Multa por ', m.tipo, ' - vencimento ', CAST(m.data_vencimento AS VARCHAR)), " +
                "m.valor, m.status " +
                "FROM multa m WHERE m.fk_cliente = ? " +
                "UNION ALL " +
                "SELECT 'PAGAMENTO', p.id_pagamento, p.data_pagamento, " +
                "CONCAT('Pagamento ', p.tipo, ' via ', p.forma_pagamento), " +
                "p.valor, CAST(NULL AS VARCHAR(45)) " +
                "FROM pagamento p WHERE p.fk_cliente = ? " +
                "UNION ALL " +
                "SELECT 'ASSINATURA', s.id_assinatura, CAST(s.data_inicio AS TIMESTAMP), " +
                "CONCAT('Plano ', pl.nome), pl.valor_mensal, s.status " +
                "FROM assinatura s JOIN plano_assinatura pl ON pl.id_plano = s.fk_plano WHERE s.fk_cliente = ?" +
                ") AS historico ORDER BY data_evento DESC, tipo_evento, id_referencia DESC";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HistoricoEventoDto.class),
                idCliente, idCliente, idCliente, idCliente);
    }

    public Integer inserirPagamento(Integer idCliente, Integer idUsuario, String tipo, BigDecimal valor,
                                    String formaPagamento, String numeroComprovante, LocalDateTime dataPagamento) {
        String sql = "INSERT INTO pagamento (fk_cliente, fk_usuario, tipo, valor, forma_pagamento, " +
                "numero_comprovante, data_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idCliente);
            ps.setInt(2, idUsuario);
            ps.setString(3, tipo);
            ps.setBigDecimal(4, valor);
            ps.setString(5, formaPagamento);
            ps.setString(6, numeroComprovante);
            ps.setObject(7, dataPagamento);
            return ps;
        }, keyHolder);

        return keyHolder.getKeyAs(Integer.class);
    }

    // Só atualiza se a multa ainda estiver pendente e com o mesmo valor_pago lido antes.
    // Retorna 0 se outra operação alterou a multa nesse meio tempo.
    public int atualizarMulta(Integer idMulta, BigDecimal valorPagoAnterior, BigDecimal novoValorPago, String novoStatus) {
        String sql = "UPDATE multa SET valor_pago = ?, status = ? " +
                "WHERE id_multa = ? AND status = 'PENDENTE' AND valor_pago = ?";

        return jdbcTemplate.update(sql, novoValorPago, novoStatus, idMulta, valorPagoAnterior);
    }

    public void inserirPagamentoMulta(Integer idPagamento, Integer idMulta, BigDecimal valorAplicado) {
        jdbcTemplate.update(
                "INSERT INTO pagamento_multa (fk_pagamento, fk_multa, valor_aplicado) VALUES (?, ?, ?)",
                idPagamento, idMulta, valorAplicado);
    }
}