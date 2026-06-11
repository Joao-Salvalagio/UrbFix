package com.urbfix.api.controllers;

import com.urbfix.api.dtos.ChamadoRequestDTO;
import com.urbfix.api.dtos.ChamadoResponseDTO;
import com.urbfix.api.dtos.EstatisticasDTO;
import com.urbfix.api.dtos.StatusUpdateRequestDTO;
import com.urbfix.api.models.EstadoChamado;
import com.urbfix.api.services.ChamadoService;
import com.urbfix.api.services.FilaPrioridadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final FilaPrioridadeService filaPrioridadeService;

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        ChamadoResponseDTO response = ChamadoResponseDTO.from(chamadoService.criarChamado(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listar(@RequestParam(required = false) EstadoChamado estado) {
        List<ChamadoResponseDTO> chamados = (estado == null
                ? chamadoService.listarTodos()
                : chamadoService.listarPorEstado(estado))
                .stream()
                .map(ChamadoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(chamados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ChamadoResponseDTO.from(chamadoService.buscarPorId(id)));
    }

    @GetMapping("/fila-prioridade")
    public ResponseEntity<List<ChamadoResponseDTO>> getFilaPrioridade() {
        List<ChamadoResponseDTO> fila = filaPrioridadeService.getFilaOrdenada().stream()
                .map(ChamadoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(fila);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasDTO> getEstatisticas() {
        return ResponseEntity.ok(EstatisticasDTO.from(chamadoService.contagemPorEstado()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                              @Valid @RequestBody StatusUpdateRequestDTO dto) {
        ChamadoResponseDTO response = ChamadoResponseDTO.from(chamadoService.atualizarStatus(id, dto.getEstado()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cidadao/{cidadaoId}")
    public ResponseEntity<List<ChamadoResponseDTO>> listarPorCidadao(@PathVariable Long cidadaoId) {
        List<ChamadoResponseDTO> chamados = chamadoService.listarPorCidadao(cidadaoId).stream()
                .map(ChamadoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(chamados);
    }
}
