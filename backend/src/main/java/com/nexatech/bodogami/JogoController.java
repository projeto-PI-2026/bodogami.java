package com.nexatech.bodogami;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/jogos")
@CrossOrigin
public class JogoController {

    private final JdbcTemplate jdbcTemplate;

    public JogoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> listarTodos() {
        String sql = "SELECT j.*, " +
                "(SELECT COUNT(id_exemplar) FROM exemplar WHERE fk_jogo = j.id_jogo) AS quantidade, " +
                "t.nome_tipo AS nome_tipo, " +
                "(SELECT STRING_AGG(g.nome, ', ') FROM jogo_genero jg JOIN genero g ON g.id_genero = jg.fk_genero WHERE jg.fk_jogo = j.id_jogo) AS nome_genero " +
                "FROM jogo j JOIN tipo_jogo t ON j.fk_tipo_jogo = t.id_tipo_jogo";

        List<Jogo> jogos = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Jogo.class));

        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Jogo>> buscarPorNome(@RequestParam String nome) {

        String sql = "SELECT *, (SELECT COUNT(id_exemplar) FROM exemplar WHERE fk_jogo = id_jogo) AS" +
                " quantidade FROM jogo WHERE LOWER(nome) LIKE LOWER(?)";

        List<Jogo> jogos = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(Jogo.class), "%" + nome + "%");

        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoJogo>> listarTipos() {
        String sql = "SELECT * FROM tipo_jogo";
        List<TipoJogo> tipos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TipoJogo.class));

        return ResponseEntity.status(200).body(tipos);
    }

    @GetMapping("/generos")
    public ResponseEntity<List<Genero>> listarGeneros() {
        String sql = "SELECT * FROM genero";
        List<Genero> generos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genero.class));
        return ResponseEntity.status(200).body(generos);
    }

    @PostMapping
    public ResponseEntity<Jogo> criarJogo(@RequestBody Jogo jogo) {

        if (jogo.getNome() == null || jogo.getNome().isBlank() || jogo.getFk_tipo_jogo() == null ||
                jogo.getValor_aluguel_diaria() == null || jogo.getMin_jogadores() == null ||
                jogo.getMax_jogadores() == null || jogo.getIdade_min() == null) {

            return ResponseEntity.status(400).build();

        }

        if (jogo.getMin_jogadores() > jogo.getMax_jogadores() ||
                jogo.getValor_aluguel_diaria() <= 0 ||
                jogo.getIdade_min() < 0 ||
                jogo.getMin_jogadores() <= 0) {
            return ResponseEntity.status(400).build();
        }

        Integer tipoExiste = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tipo_jogo WHERE id_tipo_jogo = ?", Integer.class, jogo.getFk_tipo_jogo());
        if (tipoExiste == null || tipoExiste == 0) {
            return ResponseEntity.status(400).build();
        }

        String sql = "INSERT INTO jogo (fk_tipo_jogo, nome, descricao, editora," +
                " valor_aluguel_diaria, imagem_url, min_jogadores, max_jogadores, idade_min)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, jogo.getFk_tipo_jogo());
            ps.setString(2, jogo.getNome());
            ps.setString(3, jogo.getDescricao());
            ps.setString(4, jogo.getEditora());
            ps.setDouble(5, jogo.getValor_aluguel_diaria());
            ps.setString(6, jogo.getImagem_url());
            ps.setInt(7, jogo.getMin_jogadores());
            ps.setInt(8, jogo.getMax_jogadores());
            ps.setInt(9, jogo.getIdade_min());

            return ps;
        }, keyHolder);

        Integer idGerado = keyHolder.getKeyAs(Integer.class);
        jogo.setId_jogo(idGerado);

        if (idGerado != null && jogo.getId_genero() != null) {

            String sqlRelacao = "INSERT INTO jogo_genero (fk_genero, fk_jogo) VALUES (?, ?)";

            jdbcTemplate.update(sqlRelacao, jogo.getId_genero(), idGerado);

        }

        return ResponseEntity.status(201).body(jogo);

    }

    @PostMapping("/{id}/adicionar")
    public ResponseEntity<Void> adicionarExemplar(@PathVariable Integer id) {

        Integer existe = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM jogo WHERE id_jogo = ?", Integer.class, id);
        if (existe == null || existe == 0) {
            return ResponseEntity.status(404).build();
        }

        String sql = "INSERT INTO exemplar (codigo_jogo, estado_conservacao, status, data_aquisicao, fk_jogo)" +
                " VALUES ('DEFAULT', 'Novo', 'Disponível', CURRENT_TIMESTAMP, ?)";
        //valores para exemplar mocados porque não vamos usar por agora

        jdbcTemplate.update(sql, id);

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}/remover")

    public ResponseEntity<Void> removerExemplar(@PathVariable Integer id) {

        String sql = "SELECT id_exemplar FROM exemplar WHERE fk_jogo = ? LIMIT 1";

        List<Integer> encontrados = jdbcTemplate.queryForList(sql, Integer.class, id);

        if (encontrados.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        String sqlD = "DELETE FROM exemplar WHERE id_exemplar = ?";
        jdbcTemplate.update(sqlD, encontrados.get(0));
        return ResponseEntity.status(204).build();
    }




}
