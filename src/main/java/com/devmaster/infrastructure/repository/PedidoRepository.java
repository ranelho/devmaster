package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para Pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    Optional<Pedido> findByIdAndRestauranteId(Long id, Long restauranteId);
    
    Optional<Pedido> findByIdAndClienteId(Long id, Long clienteId);
    
    List<Pedido> findByRestauranteIdOrderByCriadoEmDesc(Long restauranteId);
    
    List<Pedido> findByRestauranteIdAndStatusOrderByCriadoEmDesc(Long restauranteId, StatusPedido status);
    
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
    
    List<Pedido> findByClienteIdAndStatusOrderByCriadoEmDesc(Long clienteId, StatusPedido status);
    
    Page<Pedido> findByRestauranteId(Long restauranteId, Pageable pageable);
    
    Page<Pedido> findByRestauranteIdAndStatus(Long restauranteId, StatusPedido status, Pageable pageable);
    
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);
    
    List<Pedido> findByRestauranteIdAndCriadoEmBetween(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);
    
    boolean existsByNumeroPedido(String numeroPedido);
}
