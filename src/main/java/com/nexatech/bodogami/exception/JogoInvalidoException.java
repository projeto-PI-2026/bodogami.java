package com.nexatech.bodogami.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Dados do jogo inválidos")
public class JogoInvalidoException extends RuntimeException {

    public JogoInvalidoException() {
    }

    public JogoInvalidoException(String message) {
        super(message);
    }
}
