package com.urbfix.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cidadaos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cidadao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String dispositivoId;

    @OneToMany(mappedBy = "cidadao", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Chamado> chamados = new ArrayList<>();
}
