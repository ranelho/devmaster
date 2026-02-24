package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularEntregadorRequest;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.EntregadorRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntregadorRestauranteController implements EntregadorRestauranteAPI {
    
    private final EntregadorRestauranteService service;
    
    @Override
    public MessageResponse vincular(VincularEntregadorRequest request) {
        service.vincular(request.entregadorId(), request.restauranteId());
        return new MessageResponse("Entregador vinculado ao restaurante com sucesso");
    }
    
    @Override
    public MessageResponse desvincular(VincularEntregadorRequest request) {
        service.desvincular(request.entregadorId(), request.restauranteId());
        return new MessageResponse("Entregador desvinculado do restaurante com sucesso");
    }
    
    @Override
    public List<EntregadorResumoResponse> listarEntregadoresPorRestaurante(Long restauranteId) {
        return service.listarEntregadoresPorRestaurante(restauranteId);
    }
    
    @Override
    public List<EntregadorResumoResponse> listarEntregadoresDisponiveisPorRestaurante(Long restauranteId) {
        return service.listarEntregadoresDisponiveisPorRestaurante(restauranteId);
    }
    
    @Override
    public EntregadorResumoResponse buscarPorCpf(String cpf) {
        return service.buscarPorCpf(cpf);
    }
}
