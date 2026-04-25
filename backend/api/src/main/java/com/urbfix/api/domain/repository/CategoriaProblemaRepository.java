package com.urbfix.api.domain.repository;

import com.urbfix.api.domain.model.CategoriaProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaProblemaRepository extends JpaRepository<CategoriaProblema, Long> {
    Optional<CategoriaProblema> findByNome(String nome);
}
