package com.urbfix.api.dtos;

import com.urbfix.api.models.EstadoChamado;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequestDTO {

    @NotNull
    private EstadoChamado estado;
}
