package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para Produto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    Optional<Produto> findByIdAndRestauranteId(Long id, Long restauranteId);
    
    List<Produto> findByRestauranteIdOrderByOrdemExibicao(Long restauranteId);
    
    List<Produto> findByRestauranteIdAndDisponivelOrderByOrdemExibicao(Long restauranteId, Boolean disponivel);
    
    List<Produto> findByRestauranteIdAndDestaqueOrderByOrdemExibicao(Long restauranteId, Boolean destaque);
    
    List<Produto> findByCategoriaIdOrderByOrdemExibicao(Long categoriaId);
    
    List<Produto> findByRestauranteIdAndCategoriaIdOrderByOrdemExibicao(Long restauranteId, Long categoriaId);
    
    Page<Produto> findByRestauranteId(Long restauranteId, Pageable pageable);
    
    Page<Produto> findByRestauranteIdAndCategoriaId(Long restauranteId, Long categoriaId, Pageable pageable);
    
    boolean existsByRestauranteIdAndNome(Long restauranteId, String nome);
    
    // Métodos públicos
    List<Produto> findByRestauranteIdOrderByOrdemExibicaoAsc(Long restauranteId);
    
    List<Produto> findByRestauranteIdAndCategoriaId(Long restauranteId, Long categoriaId);
    
    List<Produto> findByRestauranteIdAndDisponivel(Long restauranteId, Boolean disponivel);
    
    List<Produto> findByRestauranteIdAndDestaque(Long restauranteId, Boolean destaque);
    
    List<Produto> findByRestauranteIdAndCategoriaIdAndDisponivel(Long restauranteId, Long categoriaId, Boolean disponivel);
    
    List<Produto> findByRestauranteIdAndDisponivelAndDestaque(Long restauranteId, Boolean disponivel, Boolean destaque);
    
    List<Produto> findByRestauranteIdAndCategoriaIdAndDisponivelAndDestaque(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque);
    
    List<Produto> findByRestauranteIdAndDestaqueTrueAndDisponivelTrueOrderByOrdemExibicaoAsc(Long restauranteId, Pageable pageable);
}
