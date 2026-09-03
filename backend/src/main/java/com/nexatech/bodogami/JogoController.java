package com.nexatech.bodogami;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
        String sql = "SELECT *, (SELECT COUNT(id_exemplar) " +
                "FROM exemplar WHERE fk_jogo = id_jogo) AS quantidade FROM jogo";

        List<Jogo> jogos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Jogo.class));

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


    //amanha irei o resto do CRUD amanha

}
