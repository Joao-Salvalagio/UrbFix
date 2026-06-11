package com.urbfix.api.repositories;

import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.EstadoChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByEstado(EstadoChamado estado);
    List<Chamado> findByEstadoOrderByCreatedAtDesc(EstadoChamado estado);
    List<Chamado> findAllByOrderByCreatedAtDesc();
    List<Chamado> findByCidadaoIdOrderByCreatedAtDesc(Long cidadaoId);
    long countByEstado(EstadoChamado estado);
}
