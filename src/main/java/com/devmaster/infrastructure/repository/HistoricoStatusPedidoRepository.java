package com.devmaster.infrastructure.repository;

import com.devmaster.domain.HistoricoStatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para HistoricoStatusPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface HistoricoStatusPedidoRepository extends JpaRepository<HistoricoStatusPedido, Long> {
    
    List<HistoricoStatusPedido> findByPedidoIdOrderByCriadoEmAsc(Long pedidoId);
}
