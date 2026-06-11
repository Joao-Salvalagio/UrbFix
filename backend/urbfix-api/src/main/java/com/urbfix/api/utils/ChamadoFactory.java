package com.urbfix.api.utils;

import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.models.EstadoChamado;
import com.urbfix.api.services.CalculadoraCriticidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChamadoFactory {

    private final CalculadoraCriticidadeService calculadoraService;

    public Chamado criarChamado(Cidadao cidadao, CategoriaProblema categoria, Double latitude, Double longitude) {
        Double score = calculadoraService.calcularScore(categoria, latitude, longitude);
        return Chamado.builder()
                .cidadao(cidadao)
                .categoria(categoria)
                .latitude(latitude)
                .longitude(longitude)
                .scorePrioridade(score)
                .estado(EstadoChamado.ABERTO)
                .build();
    }
}
