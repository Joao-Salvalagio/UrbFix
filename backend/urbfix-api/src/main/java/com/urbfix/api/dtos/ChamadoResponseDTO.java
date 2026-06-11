package com.urbfix.api.dtos;

import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.EstadoChamado;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChamadoResponseDTO {

    private Long id;
    private Long cidadaoId;
    private String cidadaoNome;
    private Long categoriaId;
    private String categoriaNome;
    private Double pesoCategoria;
    private Double latitude;
    private Double longitude;
    private Double scorePrioridade;
    private EstadoChamado estado;
    private LocalDateTime createdAt;

    public static ChamadoResponseDTO from(Chamado chamado) {
        return ChamadoResponseDTO.builder()
                .id(chamado.getId())
                .cidadaoId(chamado.getCidadao() != null ? chamado.getCidadao().getId() : null)
                .cidadaoNome(chamado.getCidadao() != null ? chamado.getCidadao().getNome() : null)
                .categoriaId(chamado.getCategoria() != null ? chamado.getCategoria().getId() : null)
                .categoriaNome(chamado.getCategoria() != null ? chamado.getCategoria().getNome() : null)
                .pesoCategoria(chamado.getCategoria() != null ? chamado.getCategoria().getPesoBase() : null)
                .latitude(chamado.getLatitude())
                .longitude(chamado.getLongitude())
                .scorePrioridade(chamado.getScorePrioridade())
                .estado(chamado.getEstado())
                .createdAt(chamado.getCreatedAt())
                .build();
    }
}
