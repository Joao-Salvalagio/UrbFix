package com.urbfix.api.application.dto;

import com.urbfix.api.domain.model.Cidadao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CidadaoDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    public static CidadaoDTO fromEntity(Cidadao cidadao) {
        return CidadaoDTO.builder()
                .id(cidadao.getId())
                .nome(cidadao.getNome())
                .email(cidadao.getEmail())
                .build();
    }
}
