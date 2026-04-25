package com.urbfix.api.application.dto;

import com.urbfix.api.domain.model.EstadoChamado;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequestDTO {
    @NotNull
    private EstadoChamado novoEstado;
}
