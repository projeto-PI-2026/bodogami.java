package com.nexatech.bodogami.dto;

public class LoginResponseDto {

    private Integer id;
    private String nome;
    private String email;
    private String tipo;
    private String token;

    public LoginResponseDto(
            Integer id,
            String nome,
            String email,
            String tipo
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}