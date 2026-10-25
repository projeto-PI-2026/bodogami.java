package com.nexatech.bodogami.dto.Cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class ClienteRequestDto {

    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 números")
    private String cpf;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    @Email(message = "Informe um e-mail válido")
    @Size(max = 120)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telefone;

    @NotBlank
    @Size(max = 255)
    private String senha;

    @NotBlank
    @Size(max = 200)
    private String logradouro;

    @NotBlank
    @Size(max = 80)
    private String bairro;

    @NotBlank
    @Size(max = 80)
    private String cidade;

    @NotNull
    @AssertTrue(message = "É necessário aceitar o termo de locação")
    private Boolean aceitouTermo;

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

    public Boolean getAceitouTermo() {
        return aceitouTermo;
    }

    public void setAceitouTermo(Boolean aceitouTermo) {
        this.aceitouTermo = aceitouTermo;
    }
}
