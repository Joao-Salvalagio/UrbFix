package com.urbfix.api.application.dto;

import com.urbfix.api.domain.model.CategoriaProblema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaProblemaDTO {
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    private String nome;

    @NotNull(message = "O peso base é obrigatório")
    private Double pesoBase;

    public static CategoriaProblemaDTO fromEntity(CategoriaProblema categoria) {
        return CategoriaProblemaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .pesoBase(categoria.getPesoBase())
                .build();
    }
}
