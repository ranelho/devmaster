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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntregadorRestController implements EntregadorAPI {
    
    private final EntregadorService entregadorService;
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public EntregadorResponse criarEntregador(EntregadorRequest request) {
        return entregadorService.criarEntregador(request);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public Page<EntregadorResumoResponse> listarEntregadores(
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        int page,
        int size
    ) {
        return entregadorService.listarEntregadores(
            ativo, disponivel, tipoVeiculo, PageRequest.of(page, size)
        );
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public EntregadorResponse buscarEntregador(Long id) {
        return entregadorService.buscarEntregador(id);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public EntregadorResponse atualizarEntregador(Long id, AtualizarEntregadorRequest request) {
        return entregadorService.atualizarEntregador(id, request);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse desativarEntregador(Long id) {
        entregadorService.desativarEntregador(id);
        return new MessageResponse("Entregador desativado com sucesso");
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse reativarEntregador(Long id) {
        entregadorService.reativarEntregador(id);
        return new MessageResponse("Entregador reativado com sucesso");
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse alterarDisponibilidade(Long id, AlterarDisponibilidadeRequest request) {
        entregadorService.alterarDisponibilidade(id, request);
        return new MessageResponse("Disponibilidade alterada com sucesso");
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        Double latitude,
        Double longitude,
        Double raioKm
    ) {
        return entregadorService.buscarEntregadoresDisponiveisProximos(
            latitude, longitude, raioKm
        );
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    public EstatisticasEntregadorResponse obterEstatisticas(Long id) {
        return entregadorService.obterEstatisticas(id);
    }
    
    @Override
    public EntregadorResumoResponse validarEntregador(Long id) {
        return entregadorService.validarEntregador(id);
    }
}
