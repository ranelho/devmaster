package com.devmaster.infrastructure.repository;

import com.devmaster.domain.EntregadorRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntregadorRestauranteRepository extends JpaRepository<EntregadorRestaurante, Long> {
    
    List<EntregadorRestaurante> findByEntregadorIdAndAtivoTrue(Long entregadorId);
    
    List<EntregadorRestaurante> findByRestauranteIdAndAtivoTrue(Long restauranteId);
    
    Optional<EntregadorRestaurante> findByEntregadorIdAndRestauranteId(Long entregadorId, Long restauranteId);
    
    boolean existsByEntregadorIdAndRestauranteIdAndAtivoTrue(Long entregadorId, Long restauranteId);
    
    @Query("SELECT er FROM EntregadorRestaurante er WHERE er.restaurante.id = :restauranteId AND er.ativo = true AND er.entregador.ativo = true AND er.entregador.disponivel = true")
    List<EntregadorRestaurante> findEntregadoresDisponiveisPorRestaurante(Long restauranteId);
}
