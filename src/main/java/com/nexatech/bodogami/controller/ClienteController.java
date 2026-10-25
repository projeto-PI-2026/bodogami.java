package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.Cliente.ClienteRequestDto;
import com.nexatech.bodogami.dto.Cliente.ClienteResponseDto;
import com.nexatech.bodogami.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexatech.bodogami.dto.LoginRequestDto;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> cadastrar(
            @Valid @RequestBody ClienteRequestDto dto) {

        ClienteResponseDto resposta = clienteService.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

}