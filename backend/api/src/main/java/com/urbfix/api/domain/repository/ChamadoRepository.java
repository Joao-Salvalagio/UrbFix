package com.urbfix.api.domain.repository;

import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.model.EstadoChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<ByEstado> findByEstado(EstadoChamado estado);
    
    interface ByEstado {
        Long getId();
        Double getScorePrioridade();
    }
    
    List<Chamado> findAllByEstado(EstadoChamado estado);
}
