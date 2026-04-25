package com.urbfix.api.domain.service;

import com.urbfix.api.domain.model.Chamado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FilaPrioridadeServiceTest {

    private FilaPrioridadeService service;

    @BeforeEach
    void setUp() {
        service = new FilaPrioridadeService();
    }

    @Test
    @DisplayName("Deve manter a ordem de prioridade (Max-Heap) ao inserir chamados com scores diferentes")
    void deveManterOrdemDePrioridade() {
        // Cenário: 3 chamados com scores diferentes
        Chamado baixoRisco = Chamado.builder().id(1L).scorePrioridade(10.0).build();
        Chamado altoRisco = Chamado.builder().id(2L).scorePrioridade(50.0).build();
        Chamado medioRisco = Chamado.builder().id(3L).scorePrioridade(30.0).build();

        // Ação
        service.enfileirar(baixoRisco);
        service.enfileirar(altoRisco);
        service.enfileirar(medioRisco);

        // Validação: A extração deve ser estritamente decrescente
        List<Chamado> filaOrdenada = service.getFilaOrdenada();
        
        assertThat(filaOrdenada).hasSize(3);
        assertThat(filaOrdenada.get(0).getScorePrioridade()).isEqualTo(50.0);
        assertThat(filaOrdenada.get(1).getScorePrioridade()).isEqualTo(30.0);
        assertThat(filaOrdenada.get(2).getScorePrioridade()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Deve remover um chamado da fila pelo ID corretamente")
    void deveRemoverChamadoDaFila() {
        Chamado c1 = Chamado.builder().id(1L).scorePrioridade(10.0).build();
        service.enfileirar(c1);
        
        service.remover(1L);
        
        assertThat(service.estaVazia()).isTrue();
    }
}
