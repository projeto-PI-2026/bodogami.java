package com.nexatech.bodogami.exception;

public class NegocioException extends RuntimeException {

    private final int status;

    public NegocioException(int status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}