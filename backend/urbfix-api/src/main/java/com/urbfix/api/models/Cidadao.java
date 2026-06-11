package com.urbfix.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cidadao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cidadao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String email;

    @Column(name = "dispositivo_id", unique = true)
    private String dispositivoId;
}
