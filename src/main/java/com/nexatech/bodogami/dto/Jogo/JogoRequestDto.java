package com.nexatech.bodogami.dto.Jogo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class JogoRequestDto {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @Size(max = 200)
    private String descricao;

    @Size(max = 45)
    private String editora;

    @NotNull
    @Positive
    private Double valor_aluguel_diaria;

    @Size(max = 200)
    private String imagem_url;

    @NotNull
    @Positive
    private Integer min_jogadores;

    @NotNull
    @Positive
    private Integer max_jogadores;

    @NotNull
    @PositiveOrZero
    private Integer idade_min;

    @NotNull
    private Integer fk_tipo_jogo;

    private Integer id_genero;

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

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Double getValor_aluguel_diaria() {
        return valor_aluguel_diaria;
    }

    public void setValor_aluguel_diaria(Double valor_aluguel_diaria) {
        this.valor_aluguel_diaria = valor_aluguel_diaria;
    }

    public String getImagem_url() {
        return imagem_url;
    }

    public void setImagem_url(String imagem_url) {
        this.imagem_url = imagem_url;
    }

    public Integer getMin_jogadores() {
        return min_jogadores;
    }

    public void setMin_jogadores(Integer min_jogadores) {
        this.min_jogadores = min_jogadores;
    }

    public Integer getMax_jogadores() {
        return max_jogadores;
    }

    public void setMax_jogadores(Integer max_jogadores) {
        this.max_jogadores = max_jogadores;
    }

    public Integer getIdade_min() {
        return idade_min;
    }

    public void setIdade_min(Integer idade_min) {
        this.idade_min = idade_min;
    }

    public Integer getFk_tipo_jogo() {
        return fk_tipo_jogo;
    }

    public void setFk_tipo_jogo(Integer fk_tipo_jogo) {
        this.fk_tipo_jogo = fk_tipo_jogo;
    }

    public Integer getId_genero() {
        return id_genero;
    }

    public void setId_genero(Integer id_genero) {
        this.id_genero = id_genero;
    }
}
