package com.nexatech.bodogami.strategy;

import com.nexatech.bodogami.exception.NegocioException;
import com.nexatech.bodogami.entity.Multa;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuitacaoTotalStrategy implements QuitacaoStrategy {

    @Override
    public String getModalidade() {
        return "TOTAL";
    }

    @Override
    public List<BigDecimal> distribuir(List<Multa> multas, BigDecimal totalDevido, BigDecimal valorRecebido) {
        if (valorRecebido.compareTo(totalDevido) != 0) {
            throw new NegocioException(400,
                    "Na quitação TOTAL o valor deve ser igual ao saldo das multas selecionadas: " + totalDevido);
        }

        List<BigDecimal> aplicacoes = new ArrayList<>();
        for (Multa multa : multas) {
            aplicacoes.add(multa.getSaldo());
        }
        return aplicacoes;
    }
}