package com.devmaster.infrastructure.repository;

import com.devmaster.domain.GrupoOpcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para GrupoOpcao.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface GrupoOpcaoRepository extends JpaRepository<GrupoOpcao, Long> {
    
    List<GrupoOpcao> findByProdutoIdOrderByOrdemExibicao(Long produtoId);
    
    Optional<GrupoOpcao> findByIdAndProdutoId(Long id, Long produtoId);
}
