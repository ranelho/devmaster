package com.devmaster.infrastructure.repository;

import com.devmaster.domain.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para TipoPagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {
    
    Optional<TipoPagamento> findByCodigo(String codigo);
    
    List<TipoPagamento> findByAtivoOrderByOrdemExibicao(Boolean ativo);
    
    boolean existsByCodigo(String codigo);
}
