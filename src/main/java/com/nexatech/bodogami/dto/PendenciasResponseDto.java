package com.nexatech.bodogami.dto;

import java.math.BigDecimal;
import java.util.List;

public class PendenciasResponseDto {

    private Integer idCliente;
    private String nomeCliente;
    private String situacaoFinanceira;
    private Integer quantidadePendencias;
    private BigDecimal valorTotalEmAberto;
    private List<PendenciaResponseDto> pendencias;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSituacaoFinanceira() {
        return situacaoFinanceira;
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public Integer getQuantidadePendencias() {
        return quantidadePendencias;
    }

    public void setQuantidadePendencias(Integer quantidadePendencias) {
        this.quantidadePendencias = quantidadePendencias;
    }

    public BigDecimal getValorTotalEmAberto() {
        return valorTotalEmAberto;
    }

    public void setValorTotalEmAberto(BigDecimal valorTotalEmAberto) {
        this.valorTotalEmAberto = valorTotalEmAberto;
    }

    public List<PendenciaResponseDto> getPendencias() {
        return pendencias;
    }

    public void setPendencias(List<PendenciaResponseDto> pendencias) {
        this.pendencias = pendencias;
    }
}