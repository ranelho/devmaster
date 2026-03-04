package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularEntregadorRequest;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.EntregadorRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntregadorRestauranteController implements EntregadorRestauranteAPI {
    
    private final EntregadorRestauranteService service;
    
    @Override
    public ResponseEntity<MessageResponse> vincular(VincularEntregadorRequest request) {
        service.vincular(request.entregadorId(), request.restauranteId());
        return ResponseEntity.ok(new MessageResponse("Entregador vinculado ao restaurante com sucesso"));
    }
    
    @Override
    public ResponseEntity<Void> desvincular(VincularEntregadorRequest request) {
        service.desvincular(request.entregadorId(), request.restauranteId());
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<List<EntregadorResumoResponse>> listarEntregadoresPorRestaurante(Long restauranteId) {
        List<EntregadorResumoResponse> response = service.listarEntregadoresPorRestaurante(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<EntregadorResumoResponse>> listarEntregadoresDisponiveisPorRestaurante(Long restauranteId) {
        List<EntregadorResumoResponse> response = service.listarEntregadoresDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EntregadorResumoResponse> buscarPorCpf(String cpf) {
        EntregadorResumoResponse response = service.buscarPorCpf(cpf);
        return ResponseEntity.ok(response);
    }
}
