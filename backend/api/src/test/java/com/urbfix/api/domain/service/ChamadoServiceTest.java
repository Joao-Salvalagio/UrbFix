package com.urbfix.api.domain.service;

import com.urbfix.api.application.dto.ChamadoRequestDTO;
import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.model.EstadoChamado;
import com.urbfix.api.domain.repository.CategoriaProblemaRepository;
import com.urbfix.api.domain.repository.ChamadoRepository;
import com.urbfix.api.domain.repository.CidadaoRepository;
import com.urbfix.api.infrastructure.factory.ChamadoFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;
    @Mock
    private CidadaoRepository cidadaoRepository;
    @Mock
    private CategoriaProblemaRepository categoriaProblemaRepository;
    @Mock
    private ChamadoFactory chamadoFactory;
    @Mock
    private FilaPrioridadeService filaPrioridadeService;

    @InjectMocks
    private ChamadoService chamadoService;

    @Test
    @DisplayName("Deve criar um chamado com sucesso e enfileirar na Max-Heap")
    void deveCriarChamadoComSucesso() {
        // Cenário
        Long cidadaoId = 1L;
        Long categoriaId = 1L;
        Cidadao cidadao = Cidadao.builder().id(cidadaoId).nome("João").build();
        CategoriaProblema categoria = CategoriaProblema.builder().id(categoriaId).nome("Buraco").build();
        Chamado chamado = Chamado.builder().id(100L).estado(EstadoChamado.ABERTO).build();

        ChamadoRequestDTO dto = new ChamadoRequestDTO();
        dto.setCidadaoId(cidadaoId);
        dto.setCategoriaId(categoriaId);
        dto.setLatitude(-23.0);
        dto.setLongitude(-46.0);
        dto.setImagemBase64("base64data");

        when(cidadaoRepository.findById(cidadaoId)).thenReturn(Optional.of(cidadao));
        when(categoriaProblemaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(chamadoFactory.criarChamado(any(), any(), any(), any())).thenReturn(chamado);
        when(chamadoRepository.save(any())).thenReturn(chamado);

        // Ação
        Chamado resultado = chamadoService.criarChamado(dto);

        // Validação
        assertThat(resultado).isNotNull();
        verify(chamadoRepository, times(1)).save(any());
        verify(filaPrioridadeService, times(1)).enfileirar(chamado);
    }

    @Test
    @DisplayName("Deve criar um cidadão anônimo se cidadaoId for nulo e dispositivoId for novo")
    void deveCriarAnonimoSeNovoDispositivo() {
        // Cenário
        Long categoriaId = 1L;
        String dispositivoId = "device123";
        CategoriaProblema categoria = CategoriaProblema.builder().id(categoriaId).nome("Buraco").build();
        Chamado chamado = Chamado.builder().id(100L).estado(EstadoChamado.ABERTO).build();

        ChamadoRequestDTO dto = new ChamadoRequestDTO();
        dto.setCategoriaId(categoriaId);
        dto.setDispositivoId(dispositivoId);
        dto.setLatitude(-23.0);
        dto.setLongitude(-46.0);

        when(cidadaoRepository.findByDispositivoId(dispositivoId)).thenReturn(Optional.empty());
        when(cidadaoRepository.save(any(Cidadao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(categoriaProblemaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(chamadoFactory.criarChamado(any(), any(), any(), any())).thenReturn(chamado);
        when(chamadoRepository.save(any())).thenReturn(chamado);

        // Ação
        Chamado resultado = chamadoService.criarChamado(dto);

        // Validação
        assertThat(resultado).isNotNull();
        verify(cidadaoRepository, times(1)).save(argThat(c -> "Anônimo".equals(c.getNome())));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar chamado para cidadão inexistente por ID")
    void deveFalharParaCidadaoInexistente() {
        ChamadoRequestDTO dto = new ChamadoRequestDTO();
        dto.setCidadaoId(1L);

        when(cidadaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadoService.criarChamado(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cidadão não encontrado");
        
        verify(chamadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover do enfileiramento ao atualizar status de ABERTO para EM_ANALISE")
    void deveRemoverDaFilaAoMudarStatus() {
        // Cenário
        Long chamadoId = 1L;
        Chamado chamado = Chamado.builder()
                .id(chamadoId)
                .estado(EstadoChamado.ABERTO)
                .build();

        when(chamadoRepository.findById(chamadoId)).thenReturn(Optional.of(chamado));
        when(chamadoRepository.save(any())).thenReturn(chamado);

        // Ação
        chamadoService.atualizarStatus(chamadoId, EstadoChamado.EM_ANALISE);

        // Validação
        assertThat(chamado.getEstado()).isEqualTo(EstadoChamado.EM_ANALISE);
        verify(filaPrioridadeService, times(1)).remover(chamadoId);
    }
}
