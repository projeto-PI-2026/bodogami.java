package com.nexatech.bodogami.service.strategy;

import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;

/**
 * Contrato das operações de autenticação.
 */
public interface AuthServiceStrategy {

    LoginResultadoDto loginCliente(LoginRequestDto dto);

    LoginResultadoDto loginUsuario(LoginRequestDto dto);
}
