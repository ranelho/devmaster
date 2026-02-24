package com.devmaster.application.service;

import com.devmaster.application.api.response.EntregadorResumoResponse;

import java.util.List;

public interface EntregadorRestauranteService {
    
    void vincular(Long entregadorId, Long restauranteId);
    
    void desvincular(Long entregadorId, Long restauranteId);
    
    List<EntregadorResumoResponse> listarEntregadoresPorRestaurante(Long restauranteId);
    
    List<EntregadorResumoResponse> listarEntregadoresDisponiveisPorRestaurante(Long restauranteId);
    
    EntregadorResumoResponse buscarPorCpf(String cpf);
}
