package com.nexatech.bodogami.service.strategy;

import com.nexatech.bodogami.dto.Cliente.ClienteRequestDto;
import com.nexatech.bodogami.dto.Cliente.ClienteResponseDto;
import com.nexatech.bodogami.dto.LoginRequestDto;

/**
 * Contrato das operações de cliente.
 */
public interface ClienteServiceStrategy {

    ClienteResponseDto cadastrar(ClienteRequestDto dto);

    String login(LoginRequestDto dto);
}
