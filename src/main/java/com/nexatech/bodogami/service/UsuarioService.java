package com.nexatech.bodogami.service;

import com.nexatech.bodogami.dto.Usuario.UsuarioRequestDto;
import com.nexatech.bodogami.dto.Usuario.UsuarioResponseDto;
import com.nexatech.bodogami.entity.Usuario;
import com.nexatech.bodogami.mapper.UsuarioMapper;
import com.nexatech.bodogami.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDto(usuarioSalvo);
    }
}