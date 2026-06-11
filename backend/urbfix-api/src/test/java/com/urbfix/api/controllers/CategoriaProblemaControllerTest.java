package com.urbfix.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbfix.api.dtos.CategoriaProblemaDTO;
import com.urbfix.api.exceptions.ResourceNotFoundException;
import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.services.CategoriaProblemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(CategoriaProblemaController.class)
class CategoriaProblemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaProblemaService categoriaService;

    private CategoriaProblema categoria;
    private CategoriaProblemaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        categoria = CategoriaProblema.builder()
                .id(1L)
                .nome("Buraco")
                .pesoBase(5.0)
                .build();

        categoriaDTO = CategoriaProblemaDTO.builder()
                .nome("Buraco")
                .pesoBase(5.0)
                .build();
    }

    @Test
    void listarTodas_DeveRetornarStatus200() throws Exception {
        when(categoriaService.listarTodas()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Buraco"));
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarStatus200() throws Exception {
        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);

        mockMvc.perform(get("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Buraco"));
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveRetornarStatus404() throws Exception {
        when(categoriaService.buscarPorId(1L)).thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        mockMvc.perform(get("/api/v1/categorias/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada"));
    }

    @Test
    void criar_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        when(categoriaService.salvar(any(CategoriaProblema.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Buraco"));
    }

    @Test
    void criar_ComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        categoriaDTO.setNome("");

        mockMvc.perform(post("/api/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nome").exists());
    }

    @Test
    void atualizar_DeveRetornarStatus200() throws Exception {
        when(categoriaService.atualizar(eq(1L), any(CategoriaProblema.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/v1/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Buraco"));
    }

    @Test
    void deletar_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
