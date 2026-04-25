package com.urbfix.api.domain.heap;

import com.urbfix.api.domain.model.Chamado;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MaxHeapTest {

    @Test
    void deveExtrairChamadoComMaiorScoreNoTopo() {
        MaxHeap maxHeap = new MaxHeap();

        Chamado baixoRisco = Chamado.builder().id(1L).scorePrioridade(10.0).build();
        Chamado altoRisco = Chamado.builder().id(2L).scorePrioridade(50.0).build();
        Chamado medioRisco = Chamado.builder().id(3L).scorePrioridade(25.0).build();

        // Inserindo em ordem não-prioritária
        maxHeap.insert(baixoRisco);
        maxHeap.insert(medioRisco);
        maxHeap.insert(altoRisco);

        Optional<Chamado> extraido = maxHeap.extractMax();

        assertTrue(extraido.isPresent());
        assertEquals(2L, extraido.get().getId());
        assertEquals(50.0, extraido.get().getScorePrioridade());
        
        extraido = maxHeap.extractMax();
        assertEquals(3L, extraido.get().getId());
        
        extraido = maxHeap.extractMax();
        assertEquals(1L, extraido.get().getId());
    }

    @Test
    void deveRemoverChamadoPorIdCorretamente() {
        MaxHeap maxHeap = new MaxHeap();
        Chamado c1 = Chamado.builder().id(1L).scorePrioridade(10.0).build();
        Chamado c2 = Chamado.builder().id(2L).scorePrioridade(20.0).build();
        
        maxHeap.insert(c1);
        maxHeap.insert(c2);
        
        maxHeap.removeById(1L);
        
        assertEquals(1, maxHeap.size());
        assertEquals(2L, maxHeap.extractMax().get().getId());
    }
}
