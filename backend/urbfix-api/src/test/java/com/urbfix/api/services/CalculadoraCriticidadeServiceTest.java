package com.urbfix.api.services;

import com.urbfix.api.models.CategoriaProblema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraCriticidadeServiceTest {

    private CalculadoraCriticidadeService service;

    @BeforeEach
    void setUp() {
        service = new CalculadoraCriticidadeService();
    }

    @Test
    void deveAtribuirScoreBaseadoNoPesoDaCategoria() {
        CategoriaProblema categoria = CategoriaProblema.builder()
                .nome("Buraco")
                .pesoBase(5.0)
                .build();

        Double score = service.calcularScore(categoria, -23.5505, -46.6333);

        assertEquals(5.0, score);
    }
}
