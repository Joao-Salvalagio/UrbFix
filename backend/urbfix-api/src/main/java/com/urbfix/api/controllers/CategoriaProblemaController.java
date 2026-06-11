package com.urbfix.api.controllers;

import com.urbfix.api.dtos.CategoriaProblemaDTO;
import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.services.CategoriaProblemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaProblemaController {

    private final CategoriaProblemaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaProblema>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProblema> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaProblema> criar(@Valid @RequestBody CategoriaProblemaDTO dto) {
        CategoriaProblema categoria = CategoriaProblema.builder()
                .nome(dto.getNome())
                .pesoBase(dto.getPesoBase())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.salvar(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProblema> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaProblemaDTO dto) {
        CategoriaProblema dados = CategoriaProblema.builder()
                .nome(dto.getNome())
                .pesoBase(dto.getPesoBase())
                .build();
        return ResponseEntity.ok(categoriaService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
