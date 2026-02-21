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
import java.util.UUID;

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
    public RestauranteResponse criarRestaurante(UUID usuarioId, RestauranteRequest request) {
        return restauranteService.criarRestaurante(usuarioId, request);
    }
    
    @Override
    public RestauranteResponse buscarRestaurante(UUID usuarioId, Long restauranteId) {
        return restauranteService.buscarRestaurante(usuarioId, restauranteId);
    }
    
    @Override
    public RestauranteResponse buscarRestaurantePorSlug(UUID usuarioId, String slug) {
        return restauranteService.buscarRestaurantePorSlug(usuarioId, slug);
    }
    
    @Override
    public Page<RestauranteResumoResponse> listarRestaurantes(
        UUID usuarioId,
        Boolean ativo,
        Boolean aberto,
        String nome,
        Pageable pageable
    ) {
        return restauranteService.listarRestaurantes(usuarioId, ativo, aberto, nome, pageable);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAbertos(UUID usuarioId, int limite) {
        return restauranteService.listarRestaurantesAbertosOrdenadosPorAvaliacao(usuarioId, limite);
    }
    
    @Override
    public RestauranteResponse atualizarRestaurante(
        UUID usuarioId,
        Long restauranteId,
        AtualizarRestauranteRequest request
    ) {
        return restauranteService.atualizarRestaurante(usuarioId, restauranteId, request);
    }
    
    @Override
    public void ativarRestaurante(UUID usuarioId, Long restauranteId) {
        restauranteService.ativarRestaurante(usuarioId, restauranteId);
    }
    
    @Override
    public void desativarRestaurante(UUID usuarioId, Long restauranteId) {
        restauranteService.desativarRestaurante(usuarioId, restauranteId);
    }
    
    @Override
    public void abrirRestaurante(UUID usuarioId, Long restauranteId) {
        restauranteService.abrirRestaurante(usuarioId, restauranteId);
    }
    
    @Override
    public void fecharRestaurante(UUID usuarioId, Long restauranteId) {
        restauranteService.fecharRestaurante(usuarioId, restauranteId);
    }
    
    @Override
    public EnderecoRestauranteResponse adicionarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        return restauranteService.adicionarEndereco(usuarioId, restauranteId, request);
    }
    
    @Override
    public EnderecoRestauranteResponse atualizarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        return restauranteService.atualizarEndereco(usuarioId, restauranteId, request);
    }
    
    @Override
    public EnderecoRestauranteResponse buscarEndereco(UUID usuarioId, Long restauranteId) {
        return restauranteService.buscarEndereco(usuarioId, restauranteId);
    }
}
