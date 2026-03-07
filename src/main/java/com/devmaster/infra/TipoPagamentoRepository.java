package com.devmaster.infra;

import com.devmaster.domain.TipoPagamento;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {

    boolean existsByCodigo(String codigo);
}
