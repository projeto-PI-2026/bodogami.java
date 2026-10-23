package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.UsuarioRequestDto;
import com.nexatech.bodogami.dto.UsuarioResponseDto;
import com.nexatech.bodogami.service.UsuarioService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody UsuarioRequestDto dto) {

        UsuarioResponseDto resposta = usuarioService.cadastrar(dto);

        return ResponseEntity.status(201).body(resposta);
    }
}