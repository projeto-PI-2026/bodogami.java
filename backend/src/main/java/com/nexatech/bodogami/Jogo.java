package com.nexatech.bodogami;

public class Jogo {
    private Integer id_jogo;
    private String nome;
    private String descricao;
    private String editora;
    private Double valor_aluguel_diaria;
    private Integer tipo_jogo;
    private String imagem_url;
    private Integer min_jogadores;
    private Integer max_jogadores;
    private Integer idade_min;
    private Integer quantidade;

    public Jogo() {
    }

    public Jogo(Integer id_jogo, String nome, String descricao,
                String editora, Double valor_aluguel_diaria, Integer tipo_jogo,
                String imagem_url, Integer min_jogadores, Integer max_jogadores,
                Integer idade_min, Integer quantidade) {

        this.id_jogo = id_jogo;
        this.nome = nome;
        this.descricao = descricao;
        this.editora = editora;
        this.valor_aluguel_diaria = valor_aluguel_diaria;
        this.tipo_jogo = tipo_jogo;
        this.imagem_url = imagem_url;
        this.min_jogadores = min_jogadores;
        this.max_jogadores = max_jogadores;
        this.idade_min = idade_min;
        this.quantidade = quantidade;

    }

    public Integer getId_jogo() {
        return id_jogo;
    }

    public void setId_jogo(Integer id_jogo) {
        this.id_jogo = id_jogo;
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

    public Integer getTipo_jogo() {
        return tipo_jogo;
    }

    public void setTipo_jogo(Integer tipo_jogo) {
        this.tipo_jogo = tipo_jogo;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
