package com.urbfix.api.services;

import com.urbfix.api.exceptions.ResourceNotFoundException;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.repositories.CidadaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CidadaoServiceTest {

    @Mock
    private CidadaoRepository cidadaoRepository;

    @InjectMocks
    private CidadaoService cidadaoService;

    private Cidadao cidadao;

    @BeforeEach
    void setUp() {
        cidadao = Cidadao.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        when(cidadaoRepository.findAll()).thenReturn(List.of(cidadao));
        List<Cidadao> result = cidadaoService.listarTodos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarCidadao() {
        when(cidadaoRepository.findById(1L)).thenReturn(Optional.of(cidadao));
        Cidadao result = cidadaoService.buscarPorId(1L);
        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        when(cidadaoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> cidadaoService.buscarPorId(1L));
    }

    @Test
    void salvar_DeveRetornarCidadaoSalvo() {
        when(cidadaoRepository.save(any(Cidadao.class))).thenReturn(cidadao);
        Cidadao result = cidadaoService.salvar(new Cidadao());
        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
    }

    @Test
    void atualizar_DeveRetornarCidadaoAtualizado() {
        Cidadao cidadaoAtualizado = Cidadao.builder().nome("João Alterado").email("joao2@email.com").build();
        when(cidadaoRepository.findById(1L)).thenReturn(Optional.of(cidadao));
        when(cidadaoRepository.save(any(Cidadao.class))).thenReturn(cidadao);

        Cidadao result = cidadaoService.atualizar(1L, cidadaoAtualizado);

        assertNotNull(result);
        assertEquals("João Alterado", result.getNome());
        verify(cidadaoRepository).save(cidadao);
    }

    @Test
    void deletar_DeveChamarDelete() {
        when(cidadaoRepository.findById(1L)).thenReturn(Optional.of(cidadao));
        cidadaoService.deletar(1L);
        verify(cidadaoRepository, times(1)).delete(cidadao);
    }
}
