package com.nexatech.bodogami.service.strategy;

import com.nexatech.bodogami.dto.Usuario.UsuarioRequestDto;
import com.nexatech.bodogami.dto.Usuario.UsuarioResponseDto;

/**
 * Contrato das operações de usuário (funcionário).
 */
public interface UsuarioServiceStrategy {

    UsuarioResponseDto cadastrar(UsuarioRequestDto dto);
}
