package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para Restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class RestauranteRestController implements RestauranteAPI {
    
    private final RestauranteService restauranteService;
    
    @Override
    public RestauranteResponse criarRestaurante(RestauranteRequest request) {
        return restauranteService.criarRestaurante(null, request);
    }
    
    @Override
    public RestauranteResponse buscarRestaurante(Long restauranteId) {
        return restauranteService.buscarRestaurante(null, restauranteId);
    }
    
    @Override
    public RestauranteResponse buscarRestaurantePorSlug(String slug) {
        return restauranteService.buscarRestaurantePorSlug(null, slug);
    }
    
    @Override
    public Page<RestauranteResumoResponse> listarRestaurantes(
        Boolean ativo,
        Boolean aberto,
        String nome,
        Pageable pageable
    ) {
        return restauranteService.listarRestaurantes(null, ativo, aberto, nome, pageable);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAbertos(int limite) {
        return restauranteService.listarRestaurantesAbertosOrdenadosPorAvaliacao(null, limite);
    }
    
    @Override
    public RestauranteResponse atualizarRestaurante(
        Long restauranteId,
        AtualizarRestauranteRequest request
    ) {
        return restauranteService.atualizarRestaurante(null, restauranteId, request);
    }
    
    @Override
    public void ativarRestaurante(Long restauranteId) {
        restauranteService.ativarRestaurante(null, restauranteId);
    }
    
    @Override
    public void desativarRestaurante(Long restauranteId) {
        restauranteService.desativarRestaurante(null, restauranteId);
    }
    
    @Override
    public void abrirRestaurante(Long restauranteId) {
        restauranteService.abrirRestaurante(null, restauranteId);
    }
    
    @Override
    public void fecharRestaurante(Long restauranteId) {
        restauranteService.fecharRestaurante(null, restauranteId);
    }
    
    @Override
    public EnderecoRestauranteResponse adicionarEndereco(
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        return restauranteService.adicionarEndereco(null, restauranteId, request);
    }
    
    @Override
    public EnderecoRestauranteResponse atualizarEndereco(
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        return restauranteService.atualizarEndereco(null, restauranteId, request);
    }
    
    @Override
    public EnderecoRestauranteResponse buscarEndereco(Long restauranteId) {
        return restauranteService.buscarEndereco(null, restauranteId);
    }
}
