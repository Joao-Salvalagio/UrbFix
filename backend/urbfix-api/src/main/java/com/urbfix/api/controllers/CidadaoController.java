package com.urbfix.api.controllers;

import com.urbfix.api.dtos.CidadaoDTO;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.services.CidadaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cidadaos")
@RequiredArgsConstructor
public class CidadaoController {

    private final CidadaoService cidadaoService;

    @GetMapping
    public ResponseEntity<List<Cidadao>> listarTodos() {
        return ResponseEntity.ok(cidadaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidadao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cidadaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cidadao> criar(@Valid @RequestBody CidadaoDTO dto) {
        Cidadao cidadao = Cidadao.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadaoService.salvar(cidadao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cidadao> atualizar(@PathVariable Long id, @Valid @RequestBody CidadaoDTO dto) {
        Cidadao dados = Cidadao.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
        return ResponseEntity.ok(cidadaoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cidadaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
