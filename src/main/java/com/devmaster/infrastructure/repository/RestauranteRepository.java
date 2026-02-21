package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para Restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    Optional<Restaurante> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByCnpj(String cnpj);
    
    boolean existsByEmail(String email);
    
    Page<Restaurante> findByAtivo(Boolean ativo, Pageable pageable);
    
    Page<Restaurante> findByAberto(Boolean aberto, Pageable pageable);
    
    Page<Restaurante> findByAtivoAndAberto(Boolean ativo, Boolean aberto, Pageable pageable);
    
    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Restaurante> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true AND r.aberto = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findRestaurantesAbertosOrdenadosPorAvaliacao(Pageable pageable);
    
    // Métodos públicos
    List<Restaurante> findByAtivoTrueAndAbertoTrueOrderByAvaliacaoDesc(Pageable pageable);
    
    List<Restaurante> findByAtivoTrueOrderByNomeAsc();
    
    List<Restaurante> findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}
