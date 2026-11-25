package com.nexatech.bodogami.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Exemplar não encontrado")
public class ExemplarNaoEncontradoException extends RuntimeException {

    public ExemplarNaoEncontradoException() {
    }

    public ExemplarNaoEncontradoException(String message) {
        super(message);
    }
}
