package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Entregador;
import com.devmaster.domain.HistoricoDisponibilidadeEntregador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de banco de dados relacionadas a HistoricoDisponibilidadeEntregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface HistoricoDisponibilidadeRepository extends JpaRepository<HistoricoDisponibilidadeEntregador, Long> {
    
    /**
     * Lista histórico de disponibilidade de um entregador com paginação.
     * 
     * @param entregador Entregador
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeEntregador> findByEntregador(Entregador entregador, Pageable pageable);
    
    /**
     * Lista histórico de disponibilidade de um entregador por ID com paginação.
     * 
     * @param entregadorId ID do entregador
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeEntregador> findByEntregadorId(Long entregadorId, Pageable pageable);
    
    /**
     * Lista histórico de disponibilidade de um entregador em um período.
     * 
     * @param entregadorId ID do entregador
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeEntregador> findByEntregadorIdAndCriadoEmBetween(
        Long entregadorId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Pageable pageable
    );
    
    /**
     * Lista histórico de disponibilidade por status.
     * 
     * @param entregadorId ID do entregador
     * @param disponivel Status de disponibilidade
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeEntregador> findByEntregadorIdAndDisponivel(
        Long entregadorId,
        Boolean disponivel,
        Pageable pageable
    );
    
    /**
     * Busca último registro de disponibilidade do entregador.
     * 
     * @param entregadorId ID do entregador
     * @return Optional contendo o último registro
     */
    @Query("""
        SELECT h FROM HistoricoDisponibilidadeEntregador h
        WHERE h.entregador.id = :entregadorId
        ORDER BY h.criadoEm DESC
        LIMIT 1
    """)
    Optional<HistoricoDisponibilidadeEntregador> findUltimoRegistro(@Param("entregadorId") Long entregadorId);
    
    /**
     * Busca última localização conhecida do entregador.
     * 
     * @param entregadorId ID do entregador
     * @return Optional contendo o último registro com localização
     */
    @Query("""
        SELECT h FROM HistoricoDisponibilidadeEntregador h
        WHERE h.entregador.id = :entregadorId
        AND h.latitude IS NOT NULL
        AND h.longitude IS NOT NULL
        ORDER BY h.criadoEm DESC
        LIMIT 1
    """)
    Optional<HistoricoDisponibilidadeEntregador> findUltimaLocalizacao(@Param("entregadorId") Long entregadorId);
    
    /**
     * Lista registros com localização de um entregador.
     * 
     * @param entregadorId ID do entregador
     * @param pageable Configuração de paginação
     * @return Página de histórico com localização
     */
    @Query("""
        SELECT h FROM HistoricoDisponibilidadeEntregador h
        WHERE h.entregador.id = :entregadorId
        AND h.latitude IS NOT NULL
        AND h.longitude IS NOT NULL
        ORDER BY h.criadoEm DESC
    """)
    Page<HistoricoDisponibilidadeEntregador> findRegistrosComLocalizacao(
        @Param("entregadorId") Long entregadorId,
        Pageable pageable
    );
    
    /**
     * Conta total de mudanças de disponibilidade de um entregador.
     * 
     * @param entregadorId ID do entregador
     * @return Total de mudanças
     */
    long countByEntregadorId(Long entregadorId);
    
    /**
     * Conta total de vezes que entregador ficou disponível.
     * 
     * @param entregadorId ID do entregador
     * @param disponivel Status de disponibilidade
     * @return Total de vezes disponível
     */
    long countByEntregadorIdAndDisponivel(Long entregadorId, Boolean disponivel);
    
    /**
     * Calcula tempo total que entregador ficou disponível em um período.
     * 
     * @param entregadorId ID do entregador
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de registros no período
     */
    @Query("""
        SELECT h FROM HistoricoDisponibilidadeEntregador h
        WHERE h.entregador.id = :entregadorId
        AND h.criadoEm BETWEEN :dataInicio AND :dataFim
        ORDER BY h.criadoEm ASC
    """)
    List<HistoricoDisponibilidadeEntregador> findRegistrosNoPeriodo(
        @Param("entregadorId") Long entregadorId,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );
}
