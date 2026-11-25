package com.nexatech.bodogami.mapper;

import com.nexatech.bodogami.dto.Jogo.GeneroResponseDto;
import com.nexatech.bodogami.dto.Jogo.JogoRequestDto;
import com.nexatech.bodogami.dto.Jogo.JogoResponseDto;
import com.nexatech.bodogami.dto.Jogo.TipoJogoResponseDto;
import com.nexatech.bodogami.entity.Genero;
import com.nexatech.bodogami.entity.Jogo;
import com.nexatech.bodogami.entity.TipoJogo;

import java.util.List;
import java.util.stream.Collectors;

public class JogoMapper {

    public static Jogo toEntity(JogoRequestDto request) {
        if (request == null) {
            return null;
        }

        Jogo jogo = new Jogo();
        jogo.setNome(request.getNome());
        jogo.setDescricao(request.getDescricao());
        jogo.setEditora(request.getEditora());
        jogo.setValorAluguelDiaria(request.getValor_aluguel_diaria());
        jogo.setImagemUrl(request.getImagem_url());
        jogo.setMinJogadores(request.getMin_jogadores());
        jogo.setMaxJogadores(request.getMax_jogadores());
        jogo.setIdadeMin(request.getIdade_min());

        TipoJogo tipoJogo = new TipoJogo();
        tipoJogo.setId(request.getFk_tipo_jogo());
        jogo.setTipoJogo(tipoJogo);

        if (request.getId_genero() != null) {
            Genero genero = new Genero();
            genero.setId(request.getId_genero());
            jogo.getGeneros().add(genero);
        }

        return jogo;
    }

    public static JogoResponseDto toResponse(Jogo jogo) {
        if (jogo == null) {
            return null;
        }

        JogoResponseDto response = new JogoResponseDto();
        response.setId_jogo(jogo.getId());
        response.setNome(jogo.getNome());
        response.setDescricao(jogo.getDescricao());
        response.setEditora(jogo.getEditora());
        response.setValor_aluguel_diaria(jogo.getValorAluguelDiaria());
        response.setImagem_url(jogo.getImagemUrl());
        response.setMin_jogadores(jogo.getMinJogadores());
        response.setMax_jogadores(jogo.getMaxJogadores());
        response.setIdade_min(jogo.getIdadeMin());
        response.setQuantidade(jogo.getQuantidade());

        if (jogo.getTipoJogo() != null) {
            response.setFk_tipo_jogo(jogo.getTipoJogo().getId());
            response.setNomeTipo(jogo.getTipoJogo().getNome_tipo());
        }

        if (jogo.getGeneros() != null && !jogo.getGeneros().isEmpty()) {
            response.setId_genero(jogo.getGeneros().get(0).getId());
            response.setNomeGenero(jogo.getGeneros().stream()
                    .map(Genero::getNome)
                    .collect(Collectors.joining(", ")));
        }

        return response;
    }

    public static List<JogoResponseDto> toResponse(List<Jogo> jogos) {
        if (jogos == null) {
            return null;
        }

        return jogos.stream()
                .map(JogoMapper::toResponse)
                .toList();
    }

    public static TipoJogoResponseDto toTipoJogoResponse(TipoJogo tipoJogo) {
        if (tipoJogo == null) {
            return null;
        }

        TipoJogoResponseDto response = new TipoJogoResponseDto();
        response.setId_tipo_jogo(tipoJogo.getId());
        response.setNome_tipo(tipoJogo.getNome_tipo());
        response.setDescricao(tipoJogo.getDescricao());

        return response;
    }

    public static List<TipoJogoResponseDto> toTipoJogoResponse(List<TipoJogo> tipos) {
        if (tipos == null) {
            return null;
        }

        return tipos.stream()
                .map(JogoMapper::toTipoJogoResponse)
                .toList();
    }

    public static GeneroResponseDto toGeneroResponse(Genero genero) {
        if (genero == null) {
            return null;
        }

        GeneroResponseDto response = new GeneroResponseDto();
        response.setId_genero(genero.getId());
        response.setNome(genero.getNome());
        response.setDescricao(genero.getDescricao());

        return response;
    }

    public static List<GeneroResponseDto> toGeneroResponse(List<Genero> generos) {
        if (generos == null) {
            return null;
        }

        return generos.stream()
                .map(JogoMapper::toGeneroResponse)
                .toList();
    }
}
