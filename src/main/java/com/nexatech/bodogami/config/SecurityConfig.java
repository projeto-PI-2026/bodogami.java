package com.nexatech.bodogami.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AutenticacaoFilter autenticacaoFilter;

    public SecurityConfig(AutenticacaoFilter autenticacaoFilter) {
        this.autenticacaoFilter = autenticacaoFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // login e logout
                        .requestMatchers(
                                "/clientes/login",
                                "/usuarios/login",
                                "/clientes/logout",
                                "/usuarios/logout"
                        ).permitAll()

                        // cadastros públicos já existentes
                        .requestMatchers(
                                HttpMethod.POST,
                                "/clientes",
                                "/usuarios"
                        ).permitAll()

                        // console H2 para desenvolvimento
                        .requestMatchers("/h2-console/**").permitAll()

                        // rotas exclusivas de cada tipo de conta
                        .requestMatchers("/clientes/**").hasRole("CLIENTE")
                        .requestMatchers("/usuarios/**").hasRole("USUARIO")

                        // outras rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        autenticacaoFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}