package com.devmaster.application.repository;

import com.devmaster.domain.RestauranteTipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteTipoPagamentoRepository extends JpaRepository<RestauranteTipoPagamento, Long> {
    
    @Query("SELECT rtp FROM RestauranteTipoPagamento rtp " +
           "WHERE rtp.restaurante.id = :restauranteId " +
           "AND rtp.ativo = true " +
           "AND rtp.tipoPagamento.ativo = true " +
           "ORDER BY rtp.ordemExibicao, rtp.tipoPagamento.nome")
    List<RestauranteTipoPagamento> findByRestauranteIdAndAtivoTrue(@Param("restauranteId") Long restauranteId);
    
    @Query("SELECT rtp FROM RestauranteTipoPagamento rtp " +
           "WHERE rtp.restaurante.id = :restauranteId " +
           "ORDER BY rtp.ordemExibicao, rtp.tipoPagamento.nome")
    List<RestauranteTipoPagamento> findByRestauranteId(@Param("restauranteId") Long restauranteId);
    
    Optional<RestauranteTipoPagamento> findByRestauranteIdAndTipoPagamentoId(Long restauranteId, Long tipoPagamentoId);
    
    boolean existsByRestauranteIdAndTipoPagamentoId(Long restauranteId, Long tipoPagamentoId);
}
