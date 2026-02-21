package com.devmaster.infrastructure.repository;

import com.devmaster.domain.HorarioRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para HorarioRestaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface HorarioRestauranteRepository extends JpaRepository<HorarioRestaurante, Long> {
    
    List<HorarioRestaurante> findByRestauranteIdOrderByDiaSemana(Long restauranteId);
    
    Optional<HorarioRestaurante> findByRestauranteIdAndDiaSemana(Long restauranteId, Integer diaSemana);
    
    boolean existsByRestauranteIdAndDiaSemana(Long restauranteId, Integer diaSemana);
    
    void deleteByRestauranteId(Long restauranteId);
    
    @Query("SELECT h FROM HorarioRestaurante h WHERE h.restaurante.id = :restauranteId AND h.fechado = false ORDER BY h.diaSemana")
    List<HorarioRestaurante> findHorariosAbertos(@Param("restauranteId") Long restauranteId);
}
