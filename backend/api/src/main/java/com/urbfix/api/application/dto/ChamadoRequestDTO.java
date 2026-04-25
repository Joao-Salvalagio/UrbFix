package com.urbfix.api.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChamadoRequestDTO {
    private Long cidadaoId;
    @NotNull
    private Long categoriaId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String dispositivoId;
    private String imagemBase64;
}
