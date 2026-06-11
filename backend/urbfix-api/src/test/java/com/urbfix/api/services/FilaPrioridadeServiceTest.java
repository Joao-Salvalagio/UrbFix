package com.urbfix.api.services;

import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.models.EstadoChamado;
import com.urbfix.api.repositories.ChamadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilaPrioridadeServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    private FilaPrioridadeService service;

    @BeforeEach
    void setUp() {
        service = new FilaPrioridadeService(chamadoRepository);
    }

    @Test
    @DisplayName("Deve manter a ordem de prioridade (Max-Heap) ao inserir chamados com scores diferentes")
    void deveManterOrdemDePrioridade() {
        Chamado baixoRisco = Chamado.builder().id(1L).scorePrioridade(10.0).build();
        Chamado altoRisco = Chamado.builder().id(2L).scorePrioridade(50.0).build();
        Chamado medioRisco = Chamado.builder().id(3L).scorePrioridade(30.0).build();

        service.enfileirar(baixoRisco);
        service.enfileirar(altoRisco);
        service.enfileirar(medioRisco);

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

    @Test
    @DisplayName("Deve rehidratar a fila com chamados ABERTO existentes no banco")
    void deveRehidratarFilaNoStartup() {
        CategoriaProblema cat = CategoriaProblema.builder().id(1L).nome("Buraco").pesoBase(5.0).build();
        Cidadao cid = Cidadao.builder().id(1L).nome("João").build();
        Chamado abertoA = Chamado.builder()
                .id(1L).scorePrioridade(5.0).estado(EstadoChamado.ABERTO)
                .categoria(cat).cidadao(cid).build();
        Chamado abertoB = Chamado.builder()
                .id(2L).scorePrioridade(15.0).estado(EstadoChamado.ABERTO)
                .categoria(cat).cidadao(cid).build();
        when(chamadoRepository.findByEstado(EstadoChamado.ABERTO)).thenReturn(List.of(abertoA, abertoB));

        service.rehidratarFila();

        assertThat(service.getFilaOrdenada().get(0).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Rehidratação deve substituir o conteúdo do heap, sem duplicar chamados já enfileirados")
    void rehidratacaoNaoDuplicaChamadosJaEnfileirados() {
        CategoriaProblema cat = CategoriaProblema.builder().id(1L).nome("Buraco").pesoBase(5.0).build();
        Chamado aberto = Chamado.builder()
                .id(1L).scorePrioridade(5.0).estado(EstadoChamado.ABERTO)
                .categoria(cat).build();
        service.enfileirar(aberto);
        when(chamadoRepository.findByEstado(EstadoChamado.ABERTO)).thenReturn(List.of(aberto));

        service.rehidratarFila();

        assertThat(service.getFilaOrdenada()).hasSize(1);
    }

    @Test
    @DisplayName("Fila inicial vazia: rehidratação sem dados não falha")
    void rehidratacaoComBancoVazioNaoFalha() {
        when(chamadoRepository.findByEstado(EstadoChamado.ABERTO)).thenReturn(Collections.emptyList());
        service.rehidratarFila();
        assertThat(service.estaVazia()).isTrue();
    }
}
