package com.urbfix.api.domain.repository;

import com.urbfix.api.domain.model.Cidadao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
    Optional<Cidadao> findByEmail(String email);
    Optional<Cidadao> findByDispositivoId(String dispositivoId);
}
