package com.nexatech.bodogami.service;

import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;
import com.nexatech.bodogami.service.strategy.AuthServiceStrategy;
import com.nexatech.bodogami.service.strategy.autenticacao.ContextoAutenticacao;
import com.nexatech.bodogami.service.strategy.autenticacao.PerfilAcesso;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceStrategy {

    private final ContextoAutenticacao contextoAutenticacao;

    public AuthService(ContextoAutenticacao contextoAutenticacao) {
        this.contextoAutenticacao = contextoAutenticacao;
    }

    @Override
    public LoginResultadoDto loginCliente(LoginRequestDto dto) {
        return contextoAutenticacao.autenticar(PerfilAcesso.CLIENTE, dto);
    }

    @Override
    public LoginResultadoDto loginUsuario(LoginRequestDto dto) {
        return contextoAutenticacao.autenticar(PerfilAcesso.USUARIO, dto);
    }
}
