package com.nexatech.bodogami.mapper;

import java.util.List;
import com.nexatech.bodogami.dto.UsuarioRequestDto;
import com.nexatech.bodogami.dto.UsuarioResponseDto;
import com.nexatech.bodogami.entity.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setCargo(dto.getCargo());
        usuario.setAtivo(dto.getAtivo());

        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario entity) {
        if (entity == null) {
            return null;
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setCargo(entity.getCargo());
        dto.setAtivo(entity.getAtivo());

        return dto;
    }

    public static List<UsuarioResponseDto> toResponseDto(List<Usuario> entities) {
        return entities.stream()
              .map(UsuarioMapper::toResponseDto)
              .toList();
    }
}
