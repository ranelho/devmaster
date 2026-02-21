package com.devmaster.application.service.impl;

import com.devmaster.application.api.response.HistoricoDisponibilidadeResponse;
import com.devmaster.application.service.DisponibilidadeService;
import com.devmaster.domain.Entregador;
import com.devmaster.domain.HistoricoDisponibilidadeEntregador;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.EntregadorRepository;
import com.devmaster.infrastructure.repository.HistoricoDisponibilidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementação do serviço de Disponibilidade.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DisponibilidadeApplicationService implements DisponibilidadeService {
    
    private final HistoricoDisponibilidadeRepository historicoRepository;
    private final EntregadorRepository entregadorRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Page<HistoricoDisponibilidadeResponse> listarHistorico(
        UUID usuarioId,
        Long entregadorId,
        Pageable pageable
    ) {
        buscarEntregadorOuFalhar(entregadorId);
        
        return historicoRepository.findByEntregadorId(entregadorId, pageable)
            .map(HistoricoDisponibilidadeResponse::from);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<HistoricoDisponibilidadeResponse> listarHistoricoPorPeriodo(
        UUID usuarioId,
        Long entregadorId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Pageable pageable
    ) {
        buscarEntregadorOuFalhar(entregadorId);
        
        if (dataInicio.isAfter(dataFim)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Data inicial deve ser anterior à data final");
        }
        
        return historicoRepository.findByEntregadorIdAndCriadoEmBetween(
            entregadorId, dataInicio, dataFim, pageable
        ).map(HistoricoDisponibilidadeResponse::from);
    }
    
    @Override
    @Transactional(readOnly = true)
    public HistoricoDisponibilidadeResponse buscarUltimoRegistro(UUID usuarioId, Long entregadorId) {
        buscarEntregadorOuFalhar(entregadorId);
        
        HistoricoDisponibilidadeEntregador historico = historicoRepository.findUltimoRegistro(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Nenhum registro de disponibilidade encontrado"));
        
        return HistoricoDisponibilidadeResponse.from(historico);
    }
    
    @Override
    @Transactional(readOnly = true)
    public HistoricoDisponibilidadeResponse buscarUltimaLocalizacao(UUID usuarioId, Long entregadorId) {
        buscarEntregadorOuFalhar(entregadorId);
        
        HistoricoDisponibilidadeEntregador historico = historicoRepository.findUltimaLocalizacao(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Nenhuma localização registrada"));
        
        return HistoricoDisponibilidadeResponse.from(historico);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<HistoricoDisponibilidadeResponse> listarRegistrosComLocalizacao(
        UUID usuarioId,
        Long entregadorId,
        Pageable pageable
    ) {
        buscarEntregadorOuFalhar(entregadorId);
        
        return historicoRepository.findRegistrosComLocalizacao(entregadorId, pageable)
            .map(HistoricoDisponibilidadeResponse::from);
    }
    
    // Métodos auxiliares
    
    private Entregador buscarEntregadorOuFalhar(Long entregadorId) {
        return entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
    }
}
