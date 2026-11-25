package com.nexatech.bodogami.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Estratégia não encontrada")
public class EstrategiaNaoEncontradaException extends RuntimeException {

    public EstrategiaNaoEncontradaException() {
    }

    public EstrategiaNaoEncontradaException(String message) {
        super(message);
    }
}
