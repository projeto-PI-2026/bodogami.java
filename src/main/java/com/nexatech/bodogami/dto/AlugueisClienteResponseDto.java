package com.nexatech.bodogami.dto;

import java.util.List;

public class AlugueisClienteResponseDto {

    private Integer idCliente;
    private Integer quantidadeAlugueis;
    private Integer quantidadeJogos;
    private List<AluguelResponseDto> alugueis;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getQuantidadeAlugueis() {
        return quantidadeAlugueis;
    }

    public void setQuantidadeAlugueis(Integer quantidadeAlugueis) {
        this.quantidadeAlugueis = quantidadeAlugueis;
    }

    public Integer getQuantidadeJogos() {
        return quantidadeJogos;
    }

    public void setQuantidadeJogos(Integer quantidadeJogos) {
        this.quantidadeJogos = quantidadeJogos;
    }

    public List<AluguelResponseDto> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<AluguelResponseDto> alugueis) {
        this.alugueis = alugueis;
    }
}