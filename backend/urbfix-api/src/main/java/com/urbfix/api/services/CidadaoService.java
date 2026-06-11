package com.urbfix.api.services;

import com.urbfix.api.exceptions.ResourceNotFoundException;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.repositories.CidadaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CidadaoService {

    private final CidadaoRepository cidadaoRepository;

    public List<Cidadao> listarTodos() {
        return cidadaoRepository.findAll();
    }

    public Cidadao buscarPorId(Long id) {
        return cidadaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com id: " + id));
    }

    public Cidadao salvar(Cidadao cidadao) {
        return cidadaoRepository.save(cidadao);
    }

    public Cidadao atualizar(Long id, Cidadao dados) {
        Cidadao cidadao = buscarPorId(id);
        cidadao.setNome(dados.getNome());
        cidadao.setEmail(dados.getEmail());
        return cidadaoRepository.save(cidadao);
    }

    public void deletar(Long id) {
        Cidadao cidadao = buscarPorId(id);
        cidadaoRepository.delete(cidadao);
    }
}
