package com.urbfix.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CidadaoDTO {

    @NotBlank
    private String nome;

    private String email;
}
