package com.devmaster.application.api;

import com.devmaster.application.api.response.HistoricoDisponibilidadeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * API REST para consulta de histórico de disponibilidade.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Disponibilidade", description = "Histórico de disponibilidade dos entregadores")
@RequestMapping("/entregadores/{entregadorId}/historico-disponibilidade")
public interface DisponibilidadeAPI {
    
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar histórico", description = "Lista histórico de disponibilidade do entregador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico de disponibilidade"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    Page<HistoricoDisponibilidadeResponse> listarHistorico(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "Data inicial (opcional)") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
        @Parameter(description = "Data final (opcional)") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "20") int size
    );
    
    @GetMapping("/ultimo")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar último registro", description = "Busca último registro de disponibilidade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Último registro encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Nenhum registro encontrado")
    })
    HistoricoDisponibilidadeResponse buscarUltimoRegistro(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId
    );
    
    @GetMapping("/ultima-localizacao")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar última localização", description = "Busca última localização conhecida do entregador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Última localização encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Nenhuma localização registrada")
    })
    HistoricoDisponibilidadeResponse buscarUltimaLocalizacao(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId
    );
    
    @GetMapping("/com-localizacao")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar registros com localização", description = "Lista registros que possuem coordenadas de localização")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros com localização"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    Page<HistoricoDisponibilidadeResponse> listarRegistrosComLocalizacao(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "20") int size
    );
}
