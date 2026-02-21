package com.devmaster.application.service;

import com.devmaster.application.api.response.HistoricoDisponibilidadeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface de serviço para operações relacionadas ao histórico de disponibilidade.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface DisponibilidadeService {
    
    /**
     * Lista histórico de disponibilidade de um entregador.
     * 
     * @param usuarioId ID do usuário que está listando
     * @param entregadorId ID do entregador
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeResponse> listarHistorico(
        UUID usuarioId,
        Long entregadorId,
        Pageable pageable
    );
    
    /**
     * Lista histórico de disponibilidade em um período.
     * 
     * @param usuarioId ID do usuário que está listando
     * @param entregadorId ID do entregador
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @param pageable Configuração de paginação
     * @return Página de histórico
     */
    Page<HistoricoDisponibilidadeResponse> listarHistoricoPorPeriodo(
        UUID usuarioId,
        Long entregadorId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Pageable pageable
    );
    
    /**
     * Busca último registro de disponibilidade.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param entregadorId ID do entregador
     * @return Último registro de disponibilidade
     */
    HistoricoDisponibilidadeResponse buscarUltimoRegistro(UUID usuarioId, Long entregadorId);
    
    /**
     * Busca última localização conhecida.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param entregadorId ID do entregador
     * @return Última localização conhecida
     */
    HistoricoDisponibilidadeResponse buscarUltimaLocalizacao(UUID usuarioId, Long entregadorId);
    
    /**
     * Lista registros com localização.
     * 
     * @param usuarioId ID do usuário que está listando
     * @param entregadorId ID do entregador
     * @param pageable Configuração de paginação
     * @return Página de registros com localização
     */
    Page<HistoricoDisponibilidadeResponse> listarRegistrosComLocalizacao(
        UUID usuarioId,
        Long entregadorId,
        Pageable pageable
    );
}
