package com.nexatech.bodogami.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuitacaoResponseDto {

    private Integer idPagamento;
    private Integer idCliente;
    private String modalidade;
    private String formaPagamento;
    private BigDecimal valorPago;
    private LocalDateTime dataPagamento;
    private Integer multasQuitadas;
    private Integer multasParcialmentePagas;
    private BigDecimal valorEmAbertoRestante;
    private String situacaoFinanceira;

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getMultasQuitadas() {
        return multasQuitadas;
    }

    public void setMultasQuitadas(Integer multasQuitadas) {
        this.multasQuitadas = multasQuitadas;
    }

    public Integer getMultasParcialmentePagas() {
        return multasParcialmentePagas;
    }

    public void setMultasParcialmentePagas(Integer multasParcialmentePagas) {
        this.multasParcialmentePagas = multasParcialmentePagas;
    }

    public BigDecimal getValorEmAbertoRestante() {
        return valorEmAbertoRestante;
    }

    public void setValorEmAbertoRestante(BigDecimal valorEmAbertoRestante) {
        this.valorEmAbertoRestante = valorEmAbertoRestante;
    }

    public String getSituacaoFinanceira() {
        return situacaoFinanceira;
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }
}