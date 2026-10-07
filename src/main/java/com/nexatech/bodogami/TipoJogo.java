package com.nexatech.bodogami;

public class TipoJogo {
    private Integer id_tipo_jogo;
    private String nome_tipo;
    private String descricao;

    public TipoJogo(Integer id_tipo_jogo, String nome_tipo, String descricao) {
        this.id_tipo_jogo = id_tipo_jogo;
        this.nome_tipo = nome_tipo;
        this.descricao = descricao;
    }

    public TipoJogo() {}

    public Integer getId_tipo_jogo() {
        return id_tipo_jogo;
    }

    public void setId_tipo_jogo(Integer id_tipo_jogo) {
        this.id_tipo_jogo = id_tipo_jogo;
    }

    public String getNome_tipo() {
        return nome_tipo;
    }

    public void setNome_tipo(String nome_tipo) {
        this.nome_tipo = nome_tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
