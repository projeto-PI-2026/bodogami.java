package com.nexatech.bodogami.mapper;

import java.util.List;
import com.nexatech.bodogami.dto.ClienteRequestDto;
import com.nexatech.bodogami.dto.ClienteResponseDto;
import com.nexatech.bodogami.entity.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setSenha(dto.getSenha());
        cliente.setLogradouro(dto.getLogradouro());
        cliente.setBairro(dto.getBairro());
        cliente.setCidade(dto.getCidade());

        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteResponseDto dto = new ClienteResponseDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setLogradouro(entity.getLogradouro());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setTermoAceitoEm(entity.getTermoAceitoEm());

        return dto;
    }

    public static List<ClienteResponseDto> toResponseDto(List<Cliente> entities) {
        return entities.stream()
              .map(ClienteMapper::toResponseDto)
              .toList();
    }
}
