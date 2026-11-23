package com.nexatech.bodogami.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class QuitacaoRequestDto {

    @NotEmpty
    @Size(max = 100)
    private List<Integer> idsMultas;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal valor;

    @NotBlank
    @Pattern(regexp = "DINHEIRO|PIX|CARTAO_DEBITO|CARTAO_CREDITO")
    private String formaPagamento;

    @NotBlank
    private String modalidade;

    @Size(max = 60)
    private String numeroComprovante;

    @NotNull
    private Integer idUsuario;

    public List<Integer> getIdsMultas() {
        return idsMultas;
    }

    public void setIdsMultas(List<Integer> idsMultas) {
        this.idsMultas = idsMultas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getNumeroComprovante() {
        return numeroComprovante;
    }

    public void setNumeroComprovante(String numeroComprovante) {
        this.numeroComprovante = numeroComprovante;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}