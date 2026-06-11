package com.urbfix.api.models;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum EstadoChamado {
    ABERTO,
    EM_ANALISE,
    EM_EXECUCAO,
    RESOLVIDO;

    private static final Map<EstadoChamado, Set<EstadoChamado>> TRANSICOES_VALIDAS = Map.of(
            ABERTO, EnumSet.of(EM_ANALISE),
            EM_ANALISE, EnumSet.of(EM_EXECUCAO, ABERTO),
            EM_EXECUCAO, EnumSet.of(RESOLVIDO, EM_ANALISE),
            RESOLVIDO, EnumSet.noneOf(EstadoChamado.class)
    );

    public boolean podeTransicionarPara(EstadoChamado destino) {
        return TRANSICOES_VALIDAS.getOrDefault(this, EnumSet.noneOf(EstadoChamado.class)).contains(destino);
    }
}
