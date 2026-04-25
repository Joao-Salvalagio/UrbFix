package com.urbfix.api.domain.model;

import java.util.Set;

public enum EstadoChamado {
    ABERTO,
    EM_ANALISE,
    EM_EXECUCAO,
    RESOLVIDO;

    public void validarTransicao(EstadoChamado novoEstado) {
        switch (this) {
            case ABERTO -> {
                if (novoEstado != EM_ANALISE && novoEstado != RESOLVIDO) {
                    throw new IllegalStateException("Transição inválida de ABERTO para " + novoEstado);
                }
            }
            case EM_ANALISE -> {
                if (novoEstado != EM_EXECUCAO && novoEstado != RESOLVIDO) {
                    throw new IllegalStateException("Transição inválida de EM_ANALISE para " + novoEstado);
                }
            }
            case EM_EXECUCAO -> {
                if (novoEstado != RESOLVIDO) {
                    throw new IllegalStateException("Transição inválida de EM_EXECUCAO para " + novoEstado);
                }
            }
            case RESOLVIDO -> {
                throw new IllegalStateException("Não é possível transitar um chamado já RESOLVIDO");
            }
        }
    }
}
