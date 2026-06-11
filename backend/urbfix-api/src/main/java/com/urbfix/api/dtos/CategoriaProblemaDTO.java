package com.urbfix.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProblemaDTO {

    @NotBlank
    private String nome;

    @NotNull
    private Double pesoBase;
}
