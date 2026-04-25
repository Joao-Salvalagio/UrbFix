package com.urbfix.api.domain.service;

import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.repository.CidadaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com o ID: " + id));
    }

    @Transactional
    public Cidadao salvar(Cidadao cidadao) {
        return cidadaoRepository.save(cidadao);
    }

    @Transactional
    public Cidadao atualizar(Long id, Cidadao cidadaoAtualizado) {
        Cidadao cidadao = buscarPorId(id);
        cidadao.setNome(cidadaoAtualizado.getNome());
        cidadao.setEmail(cidadaoAtualizado.getEmail());
        return cidadaoRepository.save(cidadao);
    }

    @Transactional
    public void deletar(Long id) {
        Cidadao cidadao = buscarPorId(id);
        cidadaoRepository.delete(cidadao);
    }
}
