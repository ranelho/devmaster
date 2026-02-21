package com.devmaster.application.api;


import com.devmaster.application.api.response.HistoricoDisponibilidadeResponse;
import com.devmaster.application.service.DisponibilidadeService;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller REST para consulta de histórico de disponibilidade.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class DisponibilidadeRestController implements DisponibilidadeAPI {
    
    private final DisponibilidadeService disponibilidadeService;
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public Page<HistoricoDisponibilidadeResponse> listarHistorico(
        Authentication authentication,
        Long entregadorId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int page,
        int size
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        
        if (dataInicio != null && dataFim != null) {
            return disponibilidadeService.listarHistoricoPorPeriodo(
                usuarioId, entregadorId, dataInicio, dataFim, PageRequest.of(page, size)
            );
        }
        
        return disponibilidadeService.listarHistorico(usuarioId, entregadorId, PageRequest.of(page, size));
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public HistoricoDisponibilidadeResponse buscarUltimoRegistro(
        Authentication authentication,
        Long entregadorId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return disponibilidadeService.buscarUltimoRegistro(usuarioId, entregadorId);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public HistoricoDisponibilidadeResponse buscarUltimaLocalizacao(
        Authentication authentication,
        Long entregadorId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return disponibilidadeService.buscarUltimaLocalizacao(usuarioId, entregadorId);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public Page<HistoricoDisponibilidadeResponse> listarRegistrosComLocalizacao(
        Authentication authentication,
        Long entregadorId,
        int page,
        int size
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return disponibilidadeService.listarRegistrosComLocalizacao(
            usuarioId, entregadorId, PageRequest.of(page, size)
        );
    }
    
    private void validarAutenticacao(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
    }
}
