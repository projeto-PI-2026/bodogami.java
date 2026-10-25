package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.Usuario.UsuarioRequestDto;
import com.nexatech.bodogami.dto.Usuario.UsuarioResponseDto;
import com.nexatech.bodogami.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@Valid @RequestBody UsuarioRequestDto dto) {

        UsuarioResponseDto resposta = usuarioService.cadastrar(dto);

        return ResponseEntity.status(201).body(resposta);
    }
}
