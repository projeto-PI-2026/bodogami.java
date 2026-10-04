package com.nexatech.bodogami.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private String senha;
    private String logradouro;
    private String bairro;
    private String cidade;
    private LocalDateTime termoAceitoEm;
    private String tokenRecuperacao;
    private LocalDateTime tokenExpiraEm;

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public LocalDateTime getTermoAceitoEm() {
        return termoAceitoEm;
    }

    public void setTermoAceitoEm(LocalDateTime termoAceitoEm) {
        this.termoAceitoEm = termoAceitoEm;
    }

    public String getTokenRecuperacao() {
        return tokenRecuperacao;
    }

    public void setTokenRecuperacao(String tokenRecuperacao) {
        this.tokenRecuperacao = tokenRecuperacao;
    }

    public LocalDateTime getTokenExpiraEm() {
        return tokenExpiraEm;
    }

    public void setTokenExpiraEm(LocalDateTime tokenExpiraEm) {
        this.tokenExpiraEm = tokenExpiraEm;
    }
}
