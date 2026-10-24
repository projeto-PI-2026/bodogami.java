package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.config.GerenciadorTokenJwt;
import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResponseDto;
import com.nexatech.bodogami.service.AuthService;

import jakarta.validation.Valid;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AuthController(AuthService authService, GerenciadorTokenJwt gerenciadorTokenJwt) {
        this.authService = authService;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    @PostMapping("/clientes/login")
    public ResponseEntity<LoginResponseDto> loginCliente(@Valid @RequestBody LoginRequestDto dto) {
        var resultado = authService.loginCliente(dto);

        ResponseCookie cookie = criarCookie(resultado.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resultado.resposta());
    }

    @PostMapping("/usuarios/login")
    public ResponseEntity<LoginResponseDto> loginUsuario(@Valid @RequestBody LoginRequestDto dto) {
        var resultado = authService.loginUsuario(dto);

        ResponseCookie cookie = criarCookie(resultado.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resultado.resposta());
    }

    @PostMapping({"/clientes/logout", "/usuarios/logout"})
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie
                .from("authToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie criarCookie(String token) {

        return ResponseCookie
                .from("authToken", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofSeconds(
                        gerenciadorTokenJwt.getValidade()
                ))
                .build();
    }
}