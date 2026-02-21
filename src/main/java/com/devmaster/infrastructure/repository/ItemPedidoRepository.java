package com.devmaster.infrastructure.repository;

import com.devmaster.domain.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para ItemPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    List<ItemPedido> findByPedidoId(Long pedidoId);
}
