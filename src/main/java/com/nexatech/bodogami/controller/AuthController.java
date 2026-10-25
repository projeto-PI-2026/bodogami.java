package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.config.GerenciadorTokenJwt;
import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResponseDto;
import com.nexatech.bodogami.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@Tag(name = "Autenticação", description = "Login e logout de clientes e usuários (JWT em cookie)")
public class AuthController {

    private final AuthService authService;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AuthController(AuthService authService, GerenciadorTokenJwt gerenciadorTokenJwt) {
        this.authService = authService;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    @PostMapping("/clientes/login")
    @Operation(summary = "Login de cliente", description = "Autentica o cliente e devolve o token JWT no cookie authToken")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha incorretos")
    })
    public ResponseEntity<LoginResponseDto> loginCliente(@Valid @RequestBody LoginRequestDto dto) {
        var resultado = authService.loginCliente(dto);

        ResponseCookie cookie = criarCookie(resultado.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resultado.resposta());
    }

    @PostMapping("/usuarios/login")
    @Operation(summary = "Login de usuário", description = "Autentica o usuário/funcionário e devolve o token JWT no cookie authToken")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha incorretos")
    })
    public ResponseEntity<LoginResponseDto> loginUsuario(@Valid @RequestBody LoginRequestDto dto) {
        var resultado = authService.loginUsuario(dto);

        ResponseCookie cookie = criarCookie(resultado.token());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resultado.resposta());
    }

    @PostMapping({"/clientes/logout", "/usuarios/logout"})
    @Operation(summary = "Encerrar sessão", description = "Invalida o cookie authToken")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sessão encerrada")
    })
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
