package com.nexatech.bodogami.service.strategy.autenticacao;

import com.nexatech.bodogami.dto.LoginRequestDto;
import com.nexatech.bodogami.dto.LoginResultadoDto;
import com.nexatech.bodogami.exception.EstrategiaNaoEncontradaException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contexto do pattern Strategy: seleciona a estratégia de autenticação
 * de acordo com o perfil informado, sem conhecer as implementações.
 */
@Component
public class ContextoAutenticacao {

    private final Map<PerfilAcesso, AutenticacaoStrategy> estrategias;

    public ContextoAutenticacao(List<AutenticacaoStrategy> estrategias) {
        this.estrategias = estrategias.stream().collect(Collectors.toUnmodifiableMap(
                AutenticacaoStrategy::perfil,
                Function.identity()
        ));
    }

    public LoginResultadoDto autenticar(PerfilAcesso perfil, LoginRequestDto dto) {
        AutenticacaoStrategy estrategia = estrategias.get(perfil);

        if (estrategia == null) {
            throw new EstrategiaNaoEncontradaException(
                    "Nenhuma estratégia de autenticação cadastrada para o perfil " + perfil
            );
        }

        return estrategia.autenticar(dto);
    }
}
