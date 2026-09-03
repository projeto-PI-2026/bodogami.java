package com.nexatech.bodogami;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
