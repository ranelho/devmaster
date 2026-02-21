package com.devmaster.infrastructure.repository;

import com.devmaster.domain.OpcaoItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para OpcaoItemPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface OpcaoItemPedidoRepository extends JpaRepository<OpcaoItemPedido, Long> {
    
    List<OpcaoItemPedido> findByItemPedidoId(Long itemPedidoId);
}
