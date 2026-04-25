package com.urbfix.api.application.controller;

import com.urbfix.api.application.dto.CidadaoDTO;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.service.CidadaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cidadaos")
@RequiredArgsConstructor
public class CidadaoController {

    private final CidadaoService cidadaoService;

    @GetMapping
    public ResponseEntity<List<CidadaoDTO>> listarTodos() {
        List<CidadaoDTO> cidadaos = cidadaoService.listarTodos().stream()
                .map(CidadaoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cidadaos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadaoDTO> buscarPorId(@PathVariable Long id) {
        Cidadao cidadao = cidadaoService.buscarPorId(id);
        return ResponseEntity.ok(CidadaoDTO.fromEntity(cidadao));
    }

    @PostMapping
    public ResponseEntity<CidadaoDTO> criar(@RequestBody @Valid CidadaoDTO dto) {
        Cidadao cidadao = Cidadao.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
        Cidadao salvo = cidadaoService.salvar(cidadao);
        return ResponseEntity.status(HttpStatus.CREATED).body(CidadaoDTO.fromEntity(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadaoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CidadaoDTO dto) {
        Cidadao cidadao = Cidadao.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
        Cidadao atualizado = cidadaoService.atualizar(id, cidadao);
        return ResponseEntity.ok(CidadaoDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cidadaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
