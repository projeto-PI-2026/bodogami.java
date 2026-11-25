package com.nexatech.bodogami.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Jogo não encontrado")
public class JogoNaoEncontradoException extends RuntimeException {

    public JogoNaoEncontradoException() {
    }

    public JogoNaoEncontradoException(String message) {
        super(message);
    }
}
