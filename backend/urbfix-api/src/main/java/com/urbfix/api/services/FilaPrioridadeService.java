package com.urbfix.api.services;

import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.EstadoChamado;
import com.urbfix.api.repositories.ChamadoRepository;
import com.urbfix.api.utils.MaxHeap;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilaPrioridadeService {

    private final ChamadoRepository chamadoRepository;
    private final MaxHeap heap = new MaxHeap();

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void rehidratarFila() {
        heap.clear();
        List<Chamado> abertos = chamadoRepository.findByEstado(EstadoChamado.ABERTO);
        abertos.forEach(chamado -> {
            chamado.getCategoria().getNome();
            if (chamado.getCidadao() != null) {
                chamado.getCidadao().getNome();
            }
            heap.insert(chamado);
        });
    }

    public void enfileirar(Chamado chamado) {
        heap.insert(chamado);
    }

    public void remover(Long id) {
        heap.removeById(id);
    }

    public List<Chamado> getFilaOrdenada() {
        return heap.toSortedList();
    }

    public boolean estaVazia() {
        return heap.isEmpty();
    }
}
