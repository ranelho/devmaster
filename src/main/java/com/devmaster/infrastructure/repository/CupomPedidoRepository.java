package com.devmaster.infrastructure.repository;

import com.devmaster.domain.CupomPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para CupomPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface CupomPedidoRepository extends JpaRepository<CupomPedido, Long> {
    
    Optional<CupomPedido> findByPedidoId(Long pedidoId);
}
