package com.nexatech.bodogami;

public class Jogo {
    private Integer id_jogo;
    private String nome;
    private String descricao;
    private String editora;
    private Double valor_aluguel_diaria;
    private String imagem_url;
    private Integer min_jogadores;
    private Integer max_jogadores;
    private Integer idade_min;
    private Integer quantidade;
    private Integer id_genero;
    private Integer fk_tipo_jogo;
    private String nomeTipo;
    private String nomeGenero;

    public Jogo (Integer id_jogo, String nome, String descricao, String editora,
                Double valor_aluguel_diaria, String imagem_url, Integer min_jogadores,
                Integer max_jogadores, Integer idade_min, Integer quantidade, Integer id_genero,
                Integer fk_tipo_jogo, String nomeTipo, String nomeGenero) {
        this.id_jogo = id_jogo;
        this.nome = nome;
        this.descricao = descricao;
        this.editora = editora;
        this.valor_aluguel_diaria = valor_aluguel_diaria;
        this.imagem_url = imagem_url;
        this.min_jogadores = min_jogadores;
        this.max_jogadores = max_jogadores;
        this.idade_min = idade_min;
        this.quantidade = quantidade;
        this.id_genero = id_genero;
        this.fk_tipo_jogo = fk_tipo_jogo;
        this.nomeTipo = nomeTipo;
        this.nomeGenero = nomeGenero;
    }

    public Jogo() {
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

    public Integer getId_genero() {
        return id_genero;
    }

    public void setId_genero(Integer id_genero) {
        this.id_genero = id_genero;
    }

    public Integer getFk_tipo_jogo() {
        return fk_tipo_jogo;
    }

    public void setFk_tipo_jogo(Integer fk_tipo_jogo) {
        this.fk_tipo_jogo = fk_tipo_jogo;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getNomeGenero() {
        return nomeGenero;
    }

    public void setNomeGenero(String nomeGenero) {
        this.nomeGenero = nomeGenero;
    }
}
