package com.urbfix.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String imagemBase64;

    @Column(nullable = false)
    private Double scorePrioridade;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoChamado estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cidadao_id")
    private Cidadao cidadao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaProblema categoria;

    public void setEstado(EstadoChamado novoEstado) {
        if (this.estado != null) {
            this.estado.validarTransicao(novoEstado);
        }
        this.estado = novoEstado;
    }
}
