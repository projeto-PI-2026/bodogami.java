package com.nexatech.bodogami.mapper;

import com.nexatech.bodogami.dto.AluguelResponseDto;
import com.nexatech.bodogami.dto.PendenciaResponseDto;
import com.nexatech.bodogami.entity.Aluguel;
import com.nexatech.bodogami.entity.Multa;

import java.util.ArrayList;
import java.util.List;

public class ClienteFinanceiroMapper {

    public static PendenciaResponseDto toPendenciaDto(Multa multa) {
        if (multa == null) {
            return null;
        }

        PendenciaResponseDto dto = new PendenciaResponseDto();
        dto.setIdMulta(multa.getId());
        dto.setTipo(multa.getTipo());
        dto.setValorOriginal(multa.getValor());
        dto.setValorPago(multa.getValorPago());
        dto.setValorEmAberto(multa.getSaldo());
        dto.setDataVencimento(multa.getDataVencimento());
        dto.setStatus(multa.getStatus());
        dto.setIdAluguel(multa.getIdAluguel());
        dto.setCodigoRetirada(multa.getCodigoRetirada());

        return dto;
    }

    public static List<PendenciaResponseDto> toPendenciaDto(List<Multa> multas) {
        List<PendenciaResponseDto> dtos = new ArrayList<>();
        for (Multa multa : multas) {
            dtos.add(toPendenciaDto(multa));
        }
        return dtos;
    }

    public static AluguelResponseDto toAluguelDto(Aluguel aluguel) {
        if (aluguel == null) {
            return null;
        }

        AluguelResponseDto dto = new AluguelResponseDto();
        dto.setId(aluguel.getId());
        dto.setCodigoRetirada(aluguel.getCodigoRetirada());
        dto.setDataAgendamento(aluguel.getDataAgendamento());
        dto.setDataRetirada(aluguel.getDataRetirada());
        dto.setDataPrevistaDevolucao(aluguel.getDataPrevistaDevolucao());
        dto.setDataDevolucao(aluguel.getDataDevolucao());
        dto.setQuantidadeJogos(aluguel.getQuantidadeJogos());
        dto.setValorTotal(aluguel.getValorTotal());
        dto.setStatus(aluguel.getStatus());

        return dto;
    }

    public static List<AluguelResponseDto> toAluguelDto(List<Aluguel> alugueis) {
        List<AluguelResponseDto> dtos = new ArrayList<>();
        for (Aluguel aluguel : alugueis) {
            dtos.add(toAluguelDto(aluguel));
        }
        return dtos;
    }
}