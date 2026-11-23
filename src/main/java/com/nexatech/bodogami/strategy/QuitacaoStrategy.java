package com.nexatech.bodogami.strategy;

import com.nexatech.bodogami.entity.Multa;

import java.math.BigDecimal;
import java.util.List;

public interface QuitacaoStrategy {

    // Identificador usado no campo "modalidade" da requisição.
    String getModalidade();

    // Devolve quanto do valor recebido será aplicado em cada multa (mesma ordem da lista).
    // Lança NegocioException (400) se o valor não for válido para a modalidade.
    List<BigDecimal> distribuir(List<Multa> multas, BigDecimal totalDevido, BigDecimal valorRecebido);
}