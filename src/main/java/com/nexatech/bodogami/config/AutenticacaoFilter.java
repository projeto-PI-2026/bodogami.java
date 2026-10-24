package com.nexatech.bodogami.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AutenticacaoFilter extends OncePerRequestFilter {

    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AutenticacaoFilter(
            GerenciadorTokenJwt gerenciadorTokenJwt
    ) {
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extrairToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = gerenciadorTokenJwt.validarToken(token);

            String email = claims.getSubject();
            String tipo = claims.get("tipo", String.class);

            if (email == null || (!"CLIENTE".equals(tipo) && !"USUARIO".equals(tipo))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String permissao = "ROLE_" + tipo;

            UsernamePasswordAuthenticationToken autenticacao =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(permissao)
                            )
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(autenticacao);

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inválido ou expirado"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())
                        && !cookie.getValue().isBlank()) {
                    return cookie.getValue();
                }
            }
        }

        String authorization = request.getHeader(
                HttpHeaders.AUTHORIZATION
        );

        if (authorization != null
                && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }
}