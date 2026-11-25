package com.nexatech.bodogami.service;

import com.nexatech.bodogami.config.GerenciadorTokenJwt;
import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResponseDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;
import com.nexatech.bodogami.entity.Cliente;
import com.nexatech.bodogami.entity.Usuario;
import com.nexatech.bodogami.repository.ClienteRepository;
import com.nexatech.bodogami.repository.UsuarioRepository;
import com.nexatech.bodogami.service.strategy.AuthServiceStrategy;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements AuthServiceStrategy {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AuthService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, GerenciadorTokenJwt gerenciadorTokenJwt) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    public LoginResultadoDto loginCliente(LoginRequestDto dto) {

        Cliente cliente = clienteRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"
                ));

        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        String token = gerenciadorTokenJwt.gerarToken(cliente.getId(), cliente.getEmail(), "CLIENTE");

        LoginResponseDto resposta = new LoginResponseDto(cliente.getId(), cliente.getNome(), cliente.getEmail(), "CLIENTE");
        resposta.setToken(token);

        return new LoginResultadoDto(token, resposta);
    }

    public LoginResultadoDto loginUsuario(LoginRequestDto dto) {

        Usuario usuario = usuarioRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"
                ));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        String token = gerenciadorTokenJwt.gerarToken(usuario.getId(), usuario.getEmail(), "USUARIO");

        LoginResponseDto resposta = new LoginResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), "USUARIO");
        resposta.setToken(token);

        return new LoginResultadoDto(token, resposta);
    }
}