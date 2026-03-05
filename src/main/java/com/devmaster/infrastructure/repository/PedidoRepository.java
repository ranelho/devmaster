package com.devmaster.infrastructure.repository;

import com.devmaster.application.api.response.ProdutoMaisVendidoDTO;
import com.devmaster.application.api.response.VendaDiariaDTO;
import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    // ==================================================================================
    // QUERIES PARA RELATÓRIOS E DASHBOARD
    // ==================================================================================

    @Query("SELECT p FROM Pedido p WHERE " +
           "p.restaurante.id = :restauranteId AND " +
           "(cast(:inicio as timestamp) IS NULL OR p.criadoEm >= :inicio) AND " +
           "(cast(:fim as timestamp) IS NULL OR p.criadoEm <= :fim) " +
           "ORDER BY p.criadoEm DESC")
    List<Pedido> findByPeriodo(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE " +
           "p.restaurante.id = :restauranteId AND " +
           "p.criadoEm BETWEEN :inicio AND :fim AND " +
           "p.status = 'ENTREGUE'")
    BigDecimal sumValorTotalByPeriodo(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE " +
           "p.restaurante.id = :restauranteId AND " +
           "p.criadoEm BETWEEN :inicio AND :fim AND " +
           "p.status = 'ENTREGUE'")
    Long countPedidosEntreguesByPeriodo(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT CAST(p.criadoEm AS date) as data, SUM(p.valorTotal) as totalVendas, COUNT(p) as quantidadePedidos " +
           "FROM Pedido p WHERE " +
           "p.restaurante.id = :restauranteId AND " +
           "p.criadoEm BETWEEN :inicio AND :fim AND " +
           "p.status = 'ENTREGUE' " +
           "GROUP BY CAST(p.criadoEm AS date) " +
           "ORDER BY CAST(p.criadoEm AS date)")
    List<VendaDiariaDTO> findVendasDiarias(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT i.produto.nome as produtoNome, SUM(i.quantidade) as quantidadeVendida, SUM(i.subtotal) as valorTotal " +
           "FROM ItemPedido i JOIN i.pedido p " +
           "WHERE p.restaurante.id = :restauranteId AND " +
           "p.criadoEm BETWEEN :inicio AND :fim AND " +
           "p.status = 'ENTREGUE' " +
           "GROUP BY i.produto.nome " +
           "ORDER BY SUM(i.quantidade) DESC")
    List<ProdutoMaisVendidoDTO> findProdutosMaisVendidos(Long restauranteId, LocalDateTime inicio, LocalDateTime fim);
}
