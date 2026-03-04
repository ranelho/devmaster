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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntregadorRestController implements EntregadorAPI {
    
    private final EntregadorService entregadorService;
    
    @Override
    public ResponseEntity<EntregadorResponse> criarEntregador(EntregadorRequest request) {
        EntregadorResponse response = entregadorService.criarEntregador(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<Page<EntregadorResumoResponse>> listarEntregadores(
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        int page,
        int size
    ) {
        Page<EntregadorResumoResponse> response = entregadorService.listarEntregadores(
            ativo, disponivel, tipoVeiculo, PageRequest.of(page, size)
        );
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EntregadorResponse> buscarEntregador(Long id) {
        EntregadorResponse response = entregadorService.buscarEntregador(id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EntregadorResponse> atualizarEntregador(Long id, AtualizarEntregadorRequest request) {
        EntregadorResponse response = entregadorService.atualizarEntregador(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> desativarEntregador(Long id) {
        entregadorService.desativarEntregador(id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<MessageResponse> reativarEntregador(Long id) {
        entregadorService.reativarEntregador(id);
        return ResponseEntity.ok(new MessageResponse("Entregador reativado com sucesso"));
    }
    
    @Override
    public ResponseEntity<MessageResponse> alterarDisponibilidade(Long id, AlterarDisponibilidadeRequest request) {
        entregadorService.alterarDisponibilidade(id, request);
        return ResponseEntity.ok(new MessageResponse("Disponibilidade alterada com sucesso"));
    }
    
    @Override
    public ResponseEntity<List<EntregadorResumoResponse>> buscarEntregadoresDisponiveisProximos(
        Double latitude,
        Double longitude,
        Double raioKm
    ) {
        List<EntregadorResumoResponse> response = entregadorService.buscarEntregadoresDisponiveisProximos(
            latitude, longitude, raioKm
        );
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EstatisticasEntregadorResponse> obterEstatisticas(Long id) {
        EstatisticasEntregadorResponse response = entregadorService.obterEstatisticas(id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EntregadorResumoResponse> validarEntregador(Long id) {
        EntregadorResumoResponse response = entregadorService.validarEntregador(id);
        return ResponseEntity.ok(response);
    }
}
