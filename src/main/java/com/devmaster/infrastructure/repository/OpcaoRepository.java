package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Opcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para Opcao.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface OpcaoRepository extends JpaRepository<Opcao, Long> {
    
    List<Opcao> findByGrupoOpcaoIdOrderByOrdemExibicao(Long grupoOpcaoId);
    
    List<Opcao> findByGrupoOpcaoIdAndDisponivelOrderByOrdemExibicao(Long grupoOpcaoId, Boolean disponivel);
    
    Optional<Opcao> findByIdAndGrupoOpcaoId(Long id, Long grupoOpcaoId);
}
