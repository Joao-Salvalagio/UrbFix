package com.urbfix.api.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbfix.api.application.dto.CidadaoDTO;
import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.service.CidadaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(CidadaoController.class)
class CidadaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private CidadaoService cidadaoService;

    private Cidadao cidadao;
    private CidadaoDTO cidadaoDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        
        cidadao = Cidadao.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@email.com")
                .build();
        
        cidadaoDTO = CidadaoDTO.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .build();
    }

    @Test
    void listarTodos_DeveRetornarStatus200() throws Exception {
        when(cidadaoService.listarTodos()).thenReturn(List.of(cidadao));

        mockMvc.perform(get("/api/v1/cidadaos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void buscarPorId_QuandoExiste_DeveRetornarStatus200() throws Exception {
        when(cidadaoService.buscarPorId(1L)).thenReturn(cidadao);

        mockMvc.perform(get("/api/v1/cidadaos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_QuandoNaoExiste_DeveRetornarStatus404() throws Exception {
        when(cidadaoService.buscarPorId(1L)).thenThrow(new ResourceNotFoundException("Não encontrado"));

        mockMvc.perform(get("/api/v1/cidadaos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Não encontrado"));
    }

    @Test
    void criar_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        when(cidadaoService.salvar(any(Cidadao.class))).thenReturn(cidadao);

        mockMvc.perform(post("/api/v1/cidadaos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cidadaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void criar_ComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        cidadaoDTO.setNome(""); // Inválido

        mockMvc.perform(post("/api/v1/cidadaos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cidadaoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nome").exists());
    }

    @Test
    void atualizar_DeveRetornarStatus200() throws Exception {
        when(cidadaoService.atualizar(eq(1L), any(Cidadao.class))).thenReturn(cidadao);

        mockMvc.perform(put("/api/v1/cidadaos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cidadaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorId_QuandoErroGenerico_DeveRetornarStatus500() throws Exception {
        when(cidadaoService.buscarPorId(1L)).thenThrow(new RuntimeException("Erro imprevisto"));

        mockMvc.perform(get("/api/v1/cidadaos/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno no servidor"));
    }

    @Test
    void deletar_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete("/api/v1/cidadaos/1"))
                .andExpect(status().isNoContent());
    }
}
