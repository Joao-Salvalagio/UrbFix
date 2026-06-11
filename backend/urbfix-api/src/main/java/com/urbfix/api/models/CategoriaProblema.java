package com.urbfix.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_problema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProblema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "peso_base", nullable = false)
    private Double pesoBase;
}
