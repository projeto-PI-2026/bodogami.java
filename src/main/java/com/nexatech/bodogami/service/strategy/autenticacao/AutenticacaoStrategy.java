package com.nexatech.bodogami.service.strategy.autenticacao;

import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;

/**
 * Estratégia de autenticação para um perfil de acesso.
 */
public interface AutenticacaoStrategy {

    PerfilAcesso perfil();

    LoginResultadoDto autenticar(LoginRequestDto dto);
}
