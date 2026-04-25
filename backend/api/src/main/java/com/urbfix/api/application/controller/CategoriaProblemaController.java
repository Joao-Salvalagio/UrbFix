package com.urbfix.api.application.controller;

import com.urbfix.api.application.dto.CategoriaProblemaDTO;
import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.service.CategoriaProblemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaProblemaController {

    private final CategoriaProblemaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaProblemaDTO>> listarTodas() {
        List<CategoriaProblemaDTO> categorias = categoriaService.listarTodas().stream()
                .map(CategoriaProblemaDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProblemaDTO> buscarPorId(@PathVariable Long id) {
        CategoriaProblema categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(CategoriaProblemaDTO.fromEntity(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaProblemaDTO> criar(@RequestBody @Valid CategoriaProblemaDTO dto) {
        CategoriaProblema categoria = CategoriaProblema.builder()
                .nome(dto.getNome())
                .pesoBase(dto.getPesoBase())
                .build();
        CategoriaProblema salva = categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaProblemaDTO.fromEntity(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProblemaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaProblemaDTO dto) {
        CategoriaProblema categoria = CategoriaProblema.builder()
                .nome(dto.getNome())
                .pesoBase(dto.getPesoBase())
                .build();
        CategoriaProblema atualizada = categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok(CategoriaProblemaDTO.fromEntity(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
