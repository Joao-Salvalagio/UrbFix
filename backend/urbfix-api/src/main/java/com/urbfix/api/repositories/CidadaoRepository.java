package com.urbfix.api.repositories;

import com.urbfix.api.models.Cidadao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
    Optional<Cidadao> findByDispositivoId(String dispositivoId);
}
