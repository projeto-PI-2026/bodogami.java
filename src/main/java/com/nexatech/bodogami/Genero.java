package com.nexatech.bodogami;

public class Genero {
    private Integer id_genero;
    private String nome;
    private String descricao;

    public Genero(Integer id_genero, String nome, String descricao) {
        this.id_genero = id_genero;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Genero() {
    }

    public Integer getId_genero() {
        return id_genero;
    }

    public void setId_genero(Integer id_genero) {
        this.id_genero = id_genero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
