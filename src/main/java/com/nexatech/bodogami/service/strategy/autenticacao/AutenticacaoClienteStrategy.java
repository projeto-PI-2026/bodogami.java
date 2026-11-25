package com.nexatech.bodogami.service.strategy.autenticacao;

import com.nexatech.bodogami.config.GerenciadorTokenJwt;
import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResponseDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;
import com.nexatech.bodogami.entity.Cliente;
import com.nexatech.bodogami.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AutenticacaoClienteStrategy implements AutenticacaoStrategy {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AutenticacaoClienteStrategy(
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder,
            GerenciadorTokenJwt gerenciadorTokenJwt
    ) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    @Override
    public PerfilAcesso perfil() {
        return PerfilAcesso.CLIENTE;
    }

    @Override
    public LoginResultadoDto autenticar(LoginRequestDto dto) {

        Cliente cliente = clienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "E-mail ou senha inválidos"
                ));

        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        String token = gerenciadorTokenJwt.gerarToken(
                cliente.getId(),
                cliente.getEmail(),
                "CLIENTE"
        );

        LoginResponseDto resposta = new LoginResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "CLIENTE"
        );
        resposta.setToken(token);

        return new LoginResultadoDto(token, resposta);
    }
}
