package com.urbfix.api.infrastructure.factory;

import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.model.EstadoChamado;
import com.urbfix.api.domain.service.CalculadoraCriticidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChamadoFactory {

    private final CalculadoraCriticidadeService calculadoraCriticidadeService;

    public Chamado criarChamado(Cidadao cidadao, CategoriaProblema categoria, Double latitude, Double longitude) {
        Double score = calculadoraCriticidadeService.calcularScore(categoria, latitude, longitude);
        
        return Chamado.builder()
                .cidadao(cidadao)
                .categoria(categoria)
                .latitude(latitude)
                .longitude(longitude)
                .scorePrioridade(score)
                .estado(EstadoChamado.ABERTO)
                .dataCriacao(LocalDateTime.now())
                .build();
    }
}
