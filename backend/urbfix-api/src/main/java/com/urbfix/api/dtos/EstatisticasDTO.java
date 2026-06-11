package com.urbfix.api.dtos;

import com.urbfix.api.models.EstadoChamado;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EstatisticasDTO {
    private long total;
    private long aberto;
    private long emAnalise;
    private long emExecucao;
    private long resolvido;

    public static EstatisticasDTO from(Map<EstadoChamado, Long> contagem) {
        long aberto = contagem.getOrDefault(EstadoChamado.ABERTO, 0L);
        long emAnalise = contagem.getOrDefault(EstadoChamado.EM_ANALISE, 0L);
        long emExecucao = contagem.getOrDefault(EstadoChamado.EM_EXECUCAO, 0L);
        long resolvido = contagem.getOrDefault(EstadoChamado.RESOLVIDO, 0L);
        return EstatisticasDTO.builder()
                .aberto(aberto)
                .emAnalise(emAnalise)
                .emExecucao(emExecucao)
                .resolvido(resolvido)
                .total(aberto + emAnalise + emExecucao + resolvido)
                .build();
    }
}
