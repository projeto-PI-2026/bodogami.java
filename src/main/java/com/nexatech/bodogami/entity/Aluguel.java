package com.nexatech.bodogami.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Aluguel {

    private Integer id;
    private String codigoRetirada;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataRetirada;
    private LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private BigDecimal valorTotal;
    private String status;
    private Integer quantidadeJogos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoRetirada() {
        return codigoRetirada;
    }

    public void setCodigoRetirada(String codigoRetirada) {
        this.codigoRetirada = codigoRetirada;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDateTime dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDateTime getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDateTime dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantidadeJogos() {
        return quantidadeJogos;
    }

    public void setQuantidadeJogos(Integer quantidadeJogos) {
        this.quantidadeJogos = quantidadeJogos;
    }
}
