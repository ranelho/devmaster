package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para Categoria.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    List<Categoria> findByRestauranteIdOrderByOrdemExibicao(Long restauranteId);
    
    List<Categoria> findByRestauranteIdAndAtivoOrderByOrdemExibicao(Long restauranteId, Boolean ativo);
    
    Page<Categoria> findByRestauranteId(Long restauranteId, Pageable pageable);
    
    Optional<Categoria> findByIdAndRestauranteId(Long id, Long restauranteId);
    
    boolean existsByRestauranteIdAndNome(Long restauranteId, String nome);
    
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.restaurante.id = :restauranteId AND c.ativo = true")
    long countCategoriasAtivasByRestaurante(@Param("restauranteId") Long restauranteId);
    
    // Métodos públicos
    List<Categoria> findByRestauranteIdAndAtivoTrueOrderByOrdemExibicaoAsc(Long restauranteId);
}
