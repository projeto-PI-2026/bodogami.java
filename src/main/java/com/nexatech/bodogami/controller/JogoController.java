package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.Jogo.GeneroResponseDto;
import com.nexatech.bodogami.dto.Jogo.JogoRequestDto;
import com.nexatech.bodogami.dto.Jogo.JogoResponseDto;
import com.nexatech.bodogami.dto.Jogo.TipoJogoResponseDto;
import com.nexatech.bodogami.entity.Jogo;
import com.nexatech.bodogami.mapper.JogoMapper;
import com.nexatech.bodogami.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jogos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @GetMapping
    public ResponseEntity<List<JogoResponseDto>> listar() {
        List<JogoResponseDto> jogos = JogoMapper.toResponse(jogoService.listar());
        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JogoResponseDto>> buscarPorNome(@RequestParam String nome) {
        List<JogoResponseDto> jogos = JogoMapper.toResponse(jogoService.buscarPorNome(nome));
        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JogoResponseDto> buscarPorId(@PathVariable Integer id) {
        Jogo jogo = jogoService.buscarPorId(id);
        return ResponseEntity.status(200).body(JogoMapper.toResponse(jogo));
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoJogoResponseDto>> listarTipos() {
        return ResponseEntity.status(200).body(JogoMapper.toTipoJogoResponse(jogoService.listarTipos()));
    }

    @GetMapping("/generos")
    public ResponseEntity<List<GeneroResponseDto>> listarGeneros() {
        return ResponseEntity.status(200).body(JogoMapper.toGeneroResponse(jogoService.listarGeneros()));
    }

    @PostMapping
    public ResponseEntity<JogoResponseDto> criar(@RequestBody @Valid JogoRequestDto request) {
        Jogo jogo = JogoMapper.toEntity(request);
        Jogo jogoCriado = jogoService.criar(jogo);
        return ResponseEntity.status(201).body(JogoMapper.toResponse(jogoCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JogoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid JogoRequestDto request) {
        Jogo jogo = JogoMapper.toEntity(request);
        Jogo jogoAtualizado = jogoService.atualizar(id, jogo);
        return ResponseEntity.status(200).body(JogoMapper.toResponse(jogoAtualizado));
    }

    @PostMapping("/{id}/adicionar")
    public ResponseEntity<Void> adicionarExemplar(@PathVariable Integer id) {
        jogoService.adicionarExemplar(id);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}/remover")
    public ResponseEntity<Void> removerExemplar(@PathVariable Integer id) {
        jogoService.removerExemplar(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        jogoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
