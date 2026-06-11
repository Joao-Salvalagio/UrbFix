package com.urbfix.api.services;

import com.urbfix.api.models.CategoriaProblema;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraCriticidadeService {

    public Double calcularScore(CategoriaProblema categoria, Double latitude, Double longitude) {
        return categoria.getPesoBase();
    }
}
