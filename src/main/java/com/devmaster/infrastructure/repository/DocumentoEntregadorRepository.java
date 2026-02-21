package com.devmaster.infrastructure.repository;

import com.devmaster.domain.DocumentoEntregador;
import com.devmaster.domain.Entregador;
import com.devmaster.domain.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de banco de dados relacionadas a DocumentoEntregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface DocumentoEntregadorRepository extends JpaRepository<DocumentoEntregador, Long> {
    
    /**
     * Lista todos os documentos de um entregador.
     * 
     * @param entregador Entregador
     * @return Lista de documentos
     */
    List<DocumentoEntregador> findByEntregador(Entregador entregador);
    
    /**
     * Lista todos os documentos de um entregador por ID.
     * 
     * @param entregadorId ID do entregador
     * @return Lista de documentos
     */
    List<DocumentoEntregador> findByEntregadorId(Long entregadorId);
    
    /**
     * Busca documento específico de um entregador por tipo.
     * 
     * @param entregador Entregador
     * @param tipoDocumento Tipo do documento
     * @return Optional contendo o documento se encontrado
     */
    Optional<DocumentoEntregador> findByEntregadorAndTipoDocumento(
        Entregador entregador, 
        TipoDocumento tipoDocumento
    );
    
    /**
     * Busca documento específico de um entregador por ID e tipo.
     * 
     * @param entregadorId ID do entregador
     * @param tipoDocumento Tipo do documento
     * @return Optional contendo o documento se encontrado
     */
    Optional<DocumentoEntregador> findByEntregadorIdAndTipoDocumento(
        Long entregadorId, 
        TipoDocumento tipoDocumento
    );
    
    /**
     * Lista documentos verificados de um entregador.
     * 
     * @param entregadorId ID do entregador
     * @param verificado Status de verificação
     * @return Lista de documentos
     */
    List<DocumentoEntregador> findByEntregadorIdAndVerificado(Long entregadorId, Boolean verificado);
    
    /**
     * Lista documentos por tipo.
     * 
     * @param tipoDocumento Tipo do documento
     * @return Lista de documentos
     */
    List<DocumentoEntregador> findByTipoDocumento(TipoDocumento tipoDocumento);
    
    /**
     * Lista documentos vencidos.
     * 
     * @param dataAtual Data atual para comparação
     * @return Lista de documentos vencidos
     */
    @Query("SELECT d FROM DocumentoEntregador d WHERE d.dataValidade < :dataAtual")
    List<DocumentoEntregador> findDocumentosVencidos(@Param("dataAtual") LocalDate dataAtual);
    
    /**
     * Lista documentos próximos do vencimento (30 dias).
     * 
     * @param dataAtual Data atual
     * @param dataLimite Data limite (30 dias)
     * @return Lista de documentos próximos do vencimento
     */
    @Query("""
        SELECT d FROM DocumentoEntregador d 
        WHERE d.dataValidade BETWEEN :dataAtual AND :dataLimite
    """)
    List<DocumentoEntregador> findDocumentosProximosVencimento(
        @Param("dataAtual") LocalDate dataAtual,
        @Param("dataLimite") LocalDate dataLimite
    );
    
    /**
     * Lista documentos não verificados.
     * 
     * @return Lista de documentos não verificados
     */
    List<DocumentoEntregador> findByVerificado(Boolean verificado);
    
    /**
     * Conta documentos de um entregador.
     * 
     * @param entregadorId ID do entregador
     * @return Total de documentos
     */
    long countByEntregadorId(Long entregadorId);
    
    /**
     * Conta documentos verificados de um entregador.
     * 
     * @param entregadorId ID do entregador
     * @param verificado Status de verificação
     * @return Total de documentos verificados
     */
    long countByEntregadorIdAndVerificado(Long entregadorId, Boolean verificado);
    
    /**
     * Verifica se entregador possui documento específico.
     * 
     * @param entregadorId ID do entregador
     * @param tipoDocumento Tipo do documento
     * @return true se possui, false caso contrário
     */
    boolean existsByEntregadorIdAndTipoDocumento(Long entregadorId, TipoDocumento tipoDocumento);
}
