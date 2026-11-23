package com.nexatech.bodogami.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PendenciaResponseDto {

    private Integer idMulta;
    private String tipo;
    private BigDecimal valorOriginal;
    private BigDecimal valorPago;
    private BigDecimal valorEmAberto;
    private LocalDate dataVencimento;
    private String status;
    private Integer idAluguel;
    private String codigoRetirada;

    public Integer getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(Integer idMulta) {
        this.idMulta = idMulta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public void setValorOriginal(BigDecimal valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getValorEmAberto() {
        return valorEmAberto;
    }

    public void setValorEmAberto(BigDecimal valorEmAberto) {
        this.valorEmAberto = valorEmAberto;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(Integer idAluguel) {
        this.idAluguel = idAluguel;
    }

    public String getCodigoRetirada() {
        return codigoRetirada;
    }

    public void setCodigoRetirada(String codigoRetirada) {
        this.codigoRetirada = codigoRetirada;
    }
}