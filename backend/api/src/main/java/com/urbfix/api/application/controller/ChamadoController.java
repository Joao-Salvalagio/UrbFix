package com.urbfix.api.application.controller;

import com.urbfix.api.application.dto.ChamadoRequestDTO;
import com.urbfix.api.application.dto.ChamadoResponseDTO;
import com.urbfix.api.application.dto.StatusUpdateRequestDTO;
import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@RequestBody @Valid ChamadoRequestDTO dto) {
        Chamado chamado = chamadoService.criarChamado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ChamadoResponseDTO.fromEntity(chamado));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listarTodos() {
        List<ChamadoResponseDTO> chamados = chamadoService.listarTodos().stream()
                .map(ChamadoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chamados);
    }

    @GetMapping("/fila-prioridade")
    public ResponseEntity<List<ChamadoResponseDTO>> getFilaPrioridade() {
        List<ChamadoResponseDTO> fila = chamadoService.getFilaPrioridade().stream()
                .map(ChamadoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fila);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid StatusUpdateRequestDTO dto) {
        Chamado chamado = chamadoService.atualizarStatus(id, dto.getNovoEstado());
        return ResponseEntity.ok(ChamadoResponseDTO.fromEntity(chamado));
    }
}
