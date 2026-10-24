package com.nexatech.bodogami.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GerenciadorTokenJwt {

    private final SecretKey chave;
    private final long validade;

    public GerenciadorTokenJwt(
            @Value("${jwt.secret}") String segredo,
            @Value("${jwt.validity}") long validade
    ) {
        this.chave = Keys.hmacShaKeyFor(
                segredo.getBytes(StandardCharsets.UTF_8)
        );

        this.validade = validade;
    }

    public String gerarToken(
            Integer id,
            String email,
            String tipo
    ) {
        Date agora = new Date();

        Date expiracao = new Date(
                agora.getTime() + validade * 1000
        );

        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .claim("tipo", tipo)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chave)
                .compact();
    }

    public Claims validarToken(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getValidade() {
        return validade;
    }
}