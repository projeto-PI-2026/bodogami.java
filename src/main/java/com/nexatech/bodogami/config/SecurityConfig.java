package com.nexatech.bodogami.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // dispatch interno de erros (400, 404, 409...)
                        .requestMatchers("/error").permitAll()

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

                        // documentação (Swagger / OpenAPI)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/webjars/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // catálogo de jogos (leitura): público
                        .requestMatchers(HttpMethod.GET, "/jogos/**").permitAll()

                        // gestão do acervo (escrita): funcionário ou cliente
                        .requestMatchers(HttpMethod.POST, "/jogos/**").hasAnyRole("USUARIO", "CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/jogos/**").hasAnyRole("USUARIO", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/jogos/**").hasAnyRole("USUARIO", "CLIENTE")

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();

        configuracao.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        configuracao.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuracao.setAllowedHeaders(List.of("*"));
        configuracao.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource origem =
                new UrlBasedCorsConfigurationSource();

        origem.registerCorsConfiguration("/**", configuracao);

        return origem;
    }
}