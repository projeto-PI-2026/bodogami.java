package com.nexatech.bodogami.controller;

import com.nexatech.bodogami.dto.AlugueisClienteResponseDto;
import com.nexatech.bodogami.dto.ErroDto;
import com.nexatech.bodogami.dto.HistoricoEventoDto;
import com.nexatech.bodogami.dto.InadimplenteResponseDto;
import com.nexatech.bodogami.dto.PendenciasResponseDto;
import com.nexatech.bodogami.dto.QuitacaoRequestDto;
import com.nexatech.bodogami.dto.QuitacaoResponseDto;
import com.nexatech.bodogami.exception.NegocioException;
import com.nexatech.bodogami.service.ClienteFinanceiroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ClienteFinanceiroController {

    private final ClienteFinanceiroService service;

    public ClienteFinanceiroController(ClienteFinanceiroService service) {
        this.service = service;
    }

    @GetMapping("/inadimplentes")
    public ResponseEntity<List<InadimplenteResponseDto>> listarInadimplentes() {
        return ResponseEntity.status(200).body(service.listarInadimplentes());
    }

    @GetMapping("/{id}/pendencias")
    public ResponseEntity<PendenciasResponseDto> consultarPendencias(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(service.consultarPendencias(id));
    }

    @GetMapping("/{id}/alugueis")
    public ResponseEntity<AlugueisClienteResponseDto> listarAlugueis(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(service.listarAlugueis(id));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoEventoDto>> consultarHistorico(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(service.consultarHistorico(id));
    }

    @PostMapping("/{id}/quitar-pendencia")
    public ResponseEntity<QuitacaoResponseDto> quitarPendencia(@PathVariable Integer id,
                                                               @Valid @RequestBody QuitacaoRequestDto dto) {
        return ResponseEntity.status(201).body(service.quitarPendencia(id, dto));
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErroDto> tratarErroDeNegocio(NegocioException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErroDto(e.getMessage()));
    }
}