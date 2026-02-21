package com.devmaster.infrastructure.repository;

import com.devmaster.domain.EnderecoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para EnderecoRestaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface EnderecoRestauranteRepository extends JpaRepository<EnderecoRestaurante, Long> {
    
    Optional<EnderecoRestaurante> findByRestauranteId(Long restauranteId);
    
    void deleteByRestauranteId(Long restauranteId);
}
