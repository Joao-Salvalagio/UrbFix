package com.urbfix.api.domain.service;

import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.repository.CategoriaProblemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaProblemaService {

    private final CategoriaProblemaRepository categoriaProblemaRepository;

    public List<CategoriaProblema> listarTodas() {
        return categoriaProblemaRepository.findAll();
    }

    public CategoriaProblema buscarPorId(Long id) {
        return categoriaProblemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de problema não encontrada com o ID: " + id));
    }

    @Transactional
    public CategoriaProblema salvar(CategoriaProblema categoria) {
        return categoriaProblemaRepository.save(categoria);
    }

    @Transactional
    public CategoriaProblema atualizar(Long id, CategoriaProblema categoriaAtualizada) {
        CategoriaProblema categoria = buscarPorId(id);
        categoria.setNome(categoriaAtualizada.getNome());
        categoria.setPesoBase(categoriaAtualizada.getPesoBase());
        return categoriaProblemaRepository.save(categoria);
    }

    @Transactional
    public void deletar(Long id) {
        CategoriaProblema categoria = buscarPorId(id);
        categoriaProblemaRepository.delete(categoria);
    }
}
