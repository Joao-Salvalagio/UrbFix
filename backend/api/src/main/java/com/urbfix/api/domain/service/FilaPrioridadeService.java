package com.urbfix.api.domain.service;

import com.urbfix.api.domain.heap.MaxHeap;
import com.urbfix.api.domain.model.Chamado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilaPrioridadeService {

    private final MaxHeap maxHeap = new MaxHeap();

    public void enfileirar(Chamado chamado) {
        maxHeap.insert(chamado);
    }

    public void remover(Long id) {
        maxHeap.removeById(id);
    }

    public List<Chamado> getFilaOrdenada() {
        return maxHeap.getAllSorted();
    }

    public boolean estaVazia() {
        return maxHeap.isEmpty();
    }

    public int tamanho() {
        return maxHeap.size();
    }
}
