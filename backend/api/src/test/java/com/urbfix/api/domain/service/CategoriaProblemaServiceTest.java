package com.urbfix.api.domain.service;

import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.repository.CategoriaProblemaRepository;
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
class CategoriaProblemaServiceTest {

    @Mock
    private CategoriaProblemaRepository categoriaRepository;

    @InjectMocks
    private CategoriaProblemaService categoriaService;

    private CategoriaProblema categoria;

    @BeforeEach
    void setUp() {
        categoria = CategoriaProblema.builder()
                .id(1L)
                .nome("Buraco")
                .pesoBase(5.0)
                .build();
    }

    @Test
    void listarTodas_DeveRetornarLista() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        List<CategoriaProblema> result = categoriaService.listarTodas();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        CategoriaProblema result = categoriaService.buscarPorId(1L);
        assertNotNull(result);
        assertEquals("Buraco", result.getNome());
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveLancarExcecao() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscarPorId(1L));
    }

    @Test
    void salvar_DeveRetornarCategoriaSalva() {
        when(categoriaRepository.save(any(CategoriaProblema.class))).thenReturn(categoria);
        CategoriaProblema result = categoriaService.salvar(new CategoriaProblema());
        assertNotNull(result);
        assertEquals("Buraco", result.getNome());
    }

    @Test
    void atualizar_DeveRetornarCategoriaAtualizada() {
        CategoriaProblema categoriaAtualizada = CategoriaProblema.builder().nome("Iluminação").pesoBase(3.0).build();
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(CategoriaProblema.class))).thenReturn(categoria);

        CategoriaProblema result = categoriaService.atualizar(1L, categoriaAtualizada);
        
        assertNotNull(result);
        assertEquals("Iluminação", result.getNome());
        assertEquals(3.0, result.getPesoBase());
    }

    @Test
    void deletar_DeveChamarDelete() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        categoriaService.deletar(1L);
        verify(categoriaRepository, times(1)).delete(categoria);
    }
}
