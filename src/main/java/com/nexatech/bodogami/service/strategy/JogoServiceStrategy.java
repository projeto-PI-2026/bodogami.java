package com.nexatech.bodogami.service.strategy;

import com.nexatech.bodogami.entity.Genero;
import com.nexatech.bodogami.entity.Jogo;
import com.nexatech.bodogami.entity.TipoJogo;

import java.util.List;

/**
 * Contrato das operações de acervo de jogos.
 */
public interface JogoServiceStrategy {

    List<Jogo> listar();

    List<Jogo> buscarPorNome(String nome);

    Jogo buscarPorId(Integer id);

    List<TipoJogo> listarTipos();

    List<Genero> listarGeneros();

    Jogo criar(Jogo jogo);

    Jogo atualizar(Integer id, Jogo jogo);

    void deletar(Integer id);

    void adicionarExemplar(Integer idJogo);

    void removerExemplar(Integer idJogo);
}
