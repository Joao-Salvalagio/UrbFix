package com.urbfix.api.application.dto;

import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.model.EstadoChamado;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChamadoResponseDTO {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Double scorePrioridade;
    private LocalDateTime dataCriacao;
    private EstadoChamado estado;
    private Long cidadaoId;
    private String nomeCidadao;
    private Long categoriaId;
    private String nomeCategoria;

    public static ChamadoResponseDTO fromEntity(Chamado chamado) {
        ChamadoResponseDTO dto = new ChamadoResponseDTO();
        dto.setId(chamado.getId());
        dto.setLatitude(chamado.getLatitude());
        dto.setLongitude(chamado.getLongitude());
        dto.setScorePrioridade(chamado.getScorePrioridade());
        dto.setDataCriacao(chamado.getDataCriacao());
        dto.setEstado(chamado.getEstado());
        
        if (chamado.getCidadao() != null) {
            dto.setCidadaoId(chamado.getCidadao().getId());
            dto.setNomeCidadao(chamado.getCidadao().getNome());
        }
        
        if (chamado.getCategoria() != null) {
            dto.setCategoriaId(chamado.getCategoria().getId());
            dto.setNomeCategoria(chamado.getCategoria().getNome());
        }
        
        return dto;
    }
}
