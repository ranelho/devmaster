package com.devmaster.infra;

import com.devmaster.domain.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {

    boolean existsByCodigo(String codigo);
}
