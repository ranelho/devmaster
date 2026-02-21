package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Entregador;
import com.devmaster.domain.enums.TipoVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de banco de dados relacionadas a Entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface EntregadorRepository extends JpaRepository<Entregador, Long> {
    
    /**
     * Busca entregador por CPF.
     * 
     * @param cpf CPF do entregador
     * @return Optional contendo o entregador se encontrado
     */
    Optional<Entregador> findByCpf(String cpf);
    
    /**
     * Busca entregador por telefone.
     * 
     * @param telefone Telefone do entregador
     * @return Optional contendo o entregador se encontrado
     */
    Optional<Entregador> findByTelefone(String telefone);
    
    /**
     * Busca entregador por email.
     * 
     * @param email Email do entregador
     * @return Optional contendo o entregador se encontrado
     */
    Optional<Entregador> findByEmail(String email);
    
    /**
     * Busca entregador por CNH.
     * 
     * @param cnh CNH do entregador
     * @return Optional contendo o entregador se encontrado
     */
    Optional<Entregador> findByCnh(String cnh);
    
    /**
     * Verifica se existe entregador com o CPF informado.
     * 
     * @param cpf CPF a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Verifica se existe entregador com o telefone informado.
     * 
     * @param telefone Telefone a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByTelefone(String telefone);
    
    /**
     * Verifica se existe entregador com o email informado.
     * 
     * @param email Email a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica se existe entregador com a CNH informada.
     * 
     * @param cnh CNH a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByCnh(String cnh);
    
    /**
     * Lista entregadores ativos com paginação.
     * 
     * @param ativo Status ativo
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<Entregador> findByAtivo(Boolean ativo, Pageable pageable);
    
    /**
     * Lista entregadores disponíveis com paginação.
     * 
     * @param disponivel Status disponível
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<Entregador> findByDisponivel(Boolean disponivel, Pageable pageable);
    
    /**
     * Lista entregadores ativos e disponíveis com paginação.
     * 
     * @param ativo Status ativo
     * @param disponivel Status disponível
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<Entregador> findByAtivoAndDisponivel(Boolean ativo, Boolean disponivel, Pageable pageable);
    
    /**
     * Lista entregadores por tipo de veículo com paginação.
     * 
     * @param tipoVeiculo Tipo de veículo
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<Entregador> findByTipoVeiculo(TipoVeiculo tipoVeiculo, Pageable pageable);
    
    /**
     * Lista entregadores ativos, disponíveis e por tipo de veículo.
     * 
     * @param ativo Status ativo
     * @param disponivel Status disponível
     * @param tipoVeiculo Tipo de veículo
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<Entregador> findByAtivoAndDisponivelAndTipoVeiculo(
        Boolean ativo, 
        Boolean disponivel, 
        TipoVeiculo tipoVeiculo, 
        Pageable pageable
    );
    
    /**
     * Busca entregadores disponíveis próximos a uma localização.
     * Usa a fórmula de Haversine para calcular distância.
     * 
     * @param latitude Latitude de referência
     * @param longitude Longitude de referência
     * @param raioKm Raio de busca em quilômetros
     * @return Lista de entregadores próximos
     */
    @Query("""
        SELECT DISTINCT e FROM Entregador e
        JOIN HistoricoDisponibilidadeEntregador h ON h.entregador.id = e.id
        WHERE e.ativo = true 
        AND e.disponivel = true
        AND h.latitude IS NOT NULL 
        AND h.longitude IS NOT NULL
        AND h.id IN (
            SELECT MAX(h2.id) 
            FROM HistoricoDisponibilidadeEntregador h2 
            WHERE h2.entregador.id = e.id
        )
        AND (
            6371 * acos(
                cos(radians(:latitude)) * 
                cos(radians(h.latitude)) * 
                cos(radians(h.longitude) - radians(:longitude)) + 
                sin(radians(:latitude)) * 
                sin(radians(h.latitude))
            )
        ) <= :raioKm
        ORDER BY (
            6371 * acos(
                cos(radians(:latitude)) * 
                cos(radians(h.latitude)) * 
                cos(radians(h.longitude) - radians(:longitude)) + 
                sin(radians(:latitude)) * 
                sin(radians(h.latitude))
            )
        )
    """)
    List<Entregador> findEntregadoresDisponiveisProximos(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("raioKm") Double raioKm
    );
    
    /**
     * Conta total de entregadores ativos.
     * 
     * @return Total de entregadores ativos
     */
    long countByAtivo(Boolean ativo);
    
    /**
     * Conta total de entregadores disponíveis.
     * 
     * @return Total de entregadores disponíveis
     */
    long countByDisponivel(Boolean disponivel);
}
