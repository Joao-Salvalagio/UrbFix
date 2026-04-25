package com.urbfix.api.domain.service;

import com.urbfix.api.domain.model.CategoriaProblema;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraCriticidadeService {

    public Double calcularScore(CategoriaProblema categoria, Double latitude, Double longitude) {
        // Implementação básica: score é o peso base da categoria.
        // Pode ser expandido futuramente com lógica geográfica ou temporal.
        return categoria.getPesoBase();
    }
}
