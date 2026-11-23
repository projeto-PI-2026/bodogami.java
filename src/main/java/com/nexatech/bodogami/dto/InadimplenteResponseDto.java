package com.nexatech.bodogami.dto;

import java.math.BigDecimal;

public class InadimplenteResponseDto {

    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String cidade;
    private String bairro;
    private String plano;
    private Integer quantidadeAlugueis;
    private Integer quantidadePendencias;
    private BigDecimal valorEmAberto;
    private String situacaoFinanceira;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public Integer getQuantidadeAlugueis() {
        return quantidadeAlugueis;
    }

    public void setQuantidadeAlugueis(Integer quantidadeAlugueis) {
        this.quantidadeAlugueis = quantidadeAlugueis;
    }

    public Integer getQuantidadePendencias() {
        return quantidadePendencias;
    }

    public void setQuantidadePendencias(Integer quantidadePendencias) {
        this.quantidadePendencias = quantidadePendencias;
    }

    public BigDecimal getValorEmAberto() {
        return valorEmAberto;
    }

    public void setValorEmAberto(BigDecimal valorEmAberto) {
        this.valorEmAberto = valorEmAberto;
    }

    public String getSituacaoFinanceira() {
        return situacaoFinanceira;
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }
}