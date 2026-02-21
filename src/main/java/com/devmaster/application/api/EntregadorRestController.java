package com.devmaster.application.api;

import com.devmaster.application.api.request.AlterarDisponibilidadeRequest;
import com.devmaster.application.api.request.AtualizarEntregadorRequest;
import com.devmaster.application.api.request.EntregadorRequest;
import com.devmaster.application.api.response.EntregadorResponse;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.EstatisticasEntregadorResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.EntregadorService;
import com.devmaster.domain.enums.TipoVeiculo;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gestão de entregadores.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class EntregadorRestController implements EntregadorAPI {
    
    private final EntregadorService entregadorService;
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public EntregadorResponse criarEntregador(Authentication authentication, EntregadorRequest request) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.criarEntregador(usuarioId, request);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public Page<EntregadorResumoResponse> listarEntregadores(
        Authentication authentication,
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        int page,
        int size
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.listarEntregadores(
            usuarioId, ativo, disponivel, tipoVeiculo, PageRequest.of(page, size)
        );
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public EntregadorResponse buscarEntregador(Authentication authentication, Long id) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.buscarEntregador(usuarioId, id);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public EntregadorResponse atualizarEntregador(
        Authentication authentication,
        Long id,
        AtualizarEntregadorRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.atualizarEntregador(usuarioId, id, request);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse desativarEntregador(Authentication authentication, Long id) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        entregadorService.desativarEntregador(usuarioId, id);
        return new MessageResponse("Entregador desativado com sucesso");
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse reativarEntregador(Authentication authentication, Long id) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        entregadorService.reativarEntregador(usuarioId, id);
        return new MessageResponse("Entregador reativado com sucesso");
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse alterarDisponibilidade(
        Authentication authentication,
        Long id,
        AlterarDisponibilidadeRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        entregadorService.alterarDisponibilidade(usuarioId, id, request);
        return new MessageResponse("Disponibilidade alterada com sucesso");
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        Authentication authentication,
        Double latitude,
        Double longitude,
        Double raioKm
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.buscarEntregadoresDisponiveisProximos(
            usuarioId, latitude, longitude, raioKm
        );
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public EstatisticasEntregadorResponse obterEstatisticas(Authentication authentication, Long id) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return entregadorService.obterEstatisticas(usuarioId, id);
    }
    
    @Override
    public EntregadorResumoResponse validarEntregador(Long id) {
        // Endpoint público para integração com módulo ENTREGA
        return entregadorService.validarEntregador(id);
    }
    
    private void validarAutenticacao(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
    }
}
