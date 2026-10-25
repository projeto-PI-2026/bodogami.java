package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.Jogo.GeneroResponseDto;
import com.nexatech.bodogami.dto.Jogo.JogoRequestDto;
import com.nexatech.bodogami.dto.Jogo.JogoResponseDto;
import com.nexatech.bodogami.dto.Jogo.TipoJogoResponseDto;
import com.nexatech.bodogami.entity.Jogo;
import com.nexatech.bodogami.mapper.JogoMapper;
import com.nexatech.bodogami.service.JogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jogos")
@Tag(name = "Jogos", description = "Cadastro e consulta do acervo de jogos de tabuleiro")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os jogos", description = "Retorna o acervo completo, com a quantidade de exemplares de cada jogo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de jogos retornada com sucesso")
    })
    public ResponseEntity<List<JogoResponseDto>> listar() {
        List<JogoResponseDto> jogos = JogoMapper.toResponse(jogoService.listar());
        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar jogos pelo nome", description = "Filtra o acervo por parte do nome do jogo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogos encontrados"),
            @ApiResponse(responseCode = "400", description = "O parâmetro de busca está em branco")
    })
    public ResponseEntity<List<JogoResponseDto>> buscarPorNome(
            @Parameter(description = "Parte do nome do jogo", example = "Catan")
            @RequestParam String nome) {
        List<JogoResponseDto> jogos = JogoMapper.toResponse(jogoService.buscarPorNome(nome));
        return ResponseEntity.status(200).body(jogos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um jogo pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogo encontrado"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    public ResponseEntity<JogoResponseDto> buscarPorId(
            @Parameter(description = "Identificador do jogo", example = "1")
            @PathVariable Integer id) {
        Jogo jogo = jogoService.buscarPorId(id);
        return ResponseEntity.status(200).body(JogoMapper.toResponse(jogo));
    }

    @GetMapping("/tipos")
    @Operation(summary = "Listar os tipos de jogo", description = "Usado para preencher o select de tipo no cadastro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tipos de jogo")
    })
    public ResponseEntity<List<TipoJogoResponseDto>> listarTipos() {
        return ResponseEntity.status(200).body(JogoMapper.toTipoJogoResponse(jogoService.listarTipos()));
    }

    @GetMapping("/generos")
    @Operation(summary = "Listar os gêneros de jogo", description = "Usado para preencher o select de gênero no cadastro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de gêneros")
    })
    public ResponseEntity<List<GeneroResponseDto>> listarGeneros() {
        return ResponseEntity.status(200).body(JogoMapper.toGeneroResponse(jogoService.listarGeneros()));
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Cadastrar um jogo", description = "Cria um novo jogo no acervo. Requer login de funcionário (ROLE_USUARIO)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Jogo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (tipo/gênero inexistente ou mínimo maior que máximo)"),
            @ApiResponse(responseCode = "403", description = "Não autenticado como funcionário")
    })
    public ResponseEntity<JogoResponseDto> criar(@RequestBody @Valid JogoRequestDto request) {
        Jogo jogo = JogoMapper.toEntity(request);
        Jogo jogoCriado = jogoService.criar(jogo);
        return ResponseEntity.status(201).body(JogoMapper.toResponse(jogoCriado));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Atualizar um jogo", description = "Altera os dados de um jogo existente. Requer login de funcionário (ROLE_USUARIO)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Não autenticado como funcionário"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    public ResponseEntity<JogoResponseDto> atualizar(
            @Parameter(description = "Identificador do jogo", example = "1")
            @PathVariable Integer id,
            @RequestBody @Valid JogoRequestDto request) {
        Jogo jogo = JogoMapper.toEntity(request);
        Jogo jogoAtualizado = jogoService.atualizar(id, jogo);
        return ResponseEntity.status(200).body(JogoMapper.toResponse(jogoAtualizado));
    }

    @PostMapping("/{id}/adicionar")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Adicionar um exemplar", description = "Adiciona uma unidade física do jogo ao estoque. Requer login de funcionário (ROLE_USUARIO)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exemplar adicionado"),
            @ApiResponse(responseCode = "403", description = "Não autenticado como funcionário"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    public ResponseEntity<Void> adicionarExemplar(
            @Parameter(description = "Identificador do jogo", example = "1")
            @PathVariable Integer id) {
        jogoService.adicionarExemplar(id);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}/remover")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Remover um exemplar", description = "Remove uma unidade física do jogo do estoque. Requer login de funcionário (ROLE_USUARIO)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exemplar removido"),
            @ApiResponse(responseCode = "403", description = "Não autenticado como funcionário"),
            @ApiResponse(responseCode = "404", description = "Jogo sem exemplares ou não encontrado")
    })
    public ResponseEntity<Void> removerExemplar(
            @Parameter(description = "Identificador do jogo", example = "1")
            @PathVariable Integer id) {
        jogoService.removerExemplar(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    @Operation(summary = "Deletar um jogo", description = "Remove o jogo e todos os seus exemplares do acervo. Requer login de funcionário (ROLE_USUARIO)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Jogo deletado"),
            @ApiResponse(responseCode = "403", description = "Não autenticado como funcionário"),
            @ApiResponse(responseCode = "404", description = "Jogo não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador do jogo", example = "1")
            @PathVariable Integer id) {
        jogoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
