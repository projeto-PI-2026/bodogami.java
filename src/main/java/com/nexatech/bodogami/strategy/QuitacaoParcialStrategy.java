package com.nexatech.bodogami.strategy;

import com.nexatech.bodogami.exception.NegocioException;
import com.nexatech.bodogami.entity.Multa;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuitacaoParcialStrategy implements QuitacaoStrategy {

    @Override
    public String getModalidade() {
        return "PARCIAL";
    }

    // Aplica o valor recebido nas multas na ordem recebida (já ordenadas por vencimento).
    @Override
    public List<BigDecimal> distribuir(List<Multa> multas, BigDecimal totalDevido, BigDecimal valorRecebido) {
        if (valorRecebido.compareTo(totalDevido) >= 0) {
            throw new NegocioException(400,
                    "Na quitação PARCIAL o valor deve ser menor que o saldo selecionado (" + totalDevido
                            + "). Para quitar tudo use a modalidade TOTAL.");
        }

        BigDecimal restante = valorRecebido;
        List<BigDecimal> aplicacoes = new ArrayList<>();
        for (Multa multa : multas) {
            BigDecimal aplicado = multa.getSaldo().min(restante);
            aplicacoes.add(aplicado);
            restante = restante.subtract(aplicado);
        }
        return aplicacoes;
    }
}