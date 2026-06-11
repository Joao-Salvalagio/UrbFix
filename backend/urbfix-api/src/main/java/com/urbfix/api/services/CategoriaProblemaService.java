package com.urbfix.api.services;

import com.urbfix.api.exceptions.ResourceNotFoundException;
import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.repositories.CategoriaProblemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaProblemaService {

    private final CategoriaProblemaRepository categoriaRepository;

    public List<CategoriaProblema> listarTodas() {
        return categoriaRepository.findAll();
    }

    public CategoriaProblema buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));
    }

    public CategoriaProblema salvar(CategoriaProblema categoria) {
        return categoriaRepository.save(categoria);
    }

    public CategoriaProblema atualizar(Long id, CategoriaProblema dados) {
        CategoriaProblema categoria = buscarPorId(id);
        categoria.setNome(dados.getNome());
        categoria.setPesoBase(dados.getPesoBase());
        return categoriaRepository.save(categoria);
    }

    public void deletar(Long id) {
        CategoriaProblema categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}
