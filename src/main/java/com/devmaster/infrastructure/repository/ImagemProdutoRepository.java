package com.devmaster.infrastructure.repository;

import com.devmaster.domain.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para ImagemProduto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {
    
    List<ImagemProduto> findByProdutoIdOrderByOrdemExibicao(Long produtoId);
    
    Optional<ImagemProduto> findByProdutoIdAndPrincipal(Long produtoId, Boolean principal);
    
    boolean existsByProdutoIdAndPrincipal(Long produtoId, Boolean principal);
}
