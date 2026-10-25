package com.nexatech.bodogami.dto.Jogo;

public class TipoJogoResponseDto {

    private Integer id_tipo_jogo;
    private String nome_tipo;
    private String descricao;

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
