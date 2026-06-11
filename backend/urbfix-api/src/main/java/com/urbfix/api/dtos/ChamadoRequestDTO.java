package com.urbfix.api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequestDTO {

    private Long cidadaoId;

    @NotNull
    private Long categoriaId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String imagemBase64;

    private String dispositivoId;
}
