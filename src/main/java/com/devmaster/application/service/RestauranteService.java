package com.devmaster.application.service;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.HorarioRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.HorarioRestauranteResponse;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface RestauranteService {
    
    RestauranteResponse criarRestaurante(UUID usuarioId, RestauranteRequest request);
    
    RestauranteResponse buscarRestaurante(UUID usuarioId, Long restauranteId);
    
    RestauranteResponse buscarRestaurantePorSlug(UUID usuarioId, String slug);
    
    Page<RestauranteResumoResponse> listarRestaurantes(
        UUID usuarioId,
        Boolean ativo,
        Boolean aberto,
        String nome,
        Pageable pageable
    );
    
    List<RestauranteResumoResponse> listarRestaurantesAbertosOrdenadosPorAvaliacao(
        UUID usuarioId,
        int limite
    );
    
    RestauranteResponse atualizarRestaurante(
        UUID usuarioId,
        Long restauranteId,
        AtualizarRestauranteRequest request
    );
    
    void ativarRestaurante(UUID usuarioId, Long restauranteId);
    
    void desativarRestaurante(UUID usuarioId, Long restauranteId);
    
    void abrirRestaurante(UUID usuarioId, Long restauranteId);
    
    void fecharRestaurante(UUID usuarioId, Long restauranteId);
    
    EnderecoRestauranteResponse adicionarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    );
    
    EnderecoRestauranteResponse atualizarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    );
    
    EnderecoRestauranteResponse buscarEndereco(UUID usuarioId, Long restauranteId);
    
    // Horários
    
    HorarioRestauranteResponse adicionarHorario(
        UUID usuarioId,
        Long restauranteId,
        HorarioRestauranteRequest request
    );
    
    List<HorarioRestauranteResponse> listarHorarios(UUID usuarioId, Long restauranteId);
    
    HorarioRestauranteResponse atualizarHorario(
        UUID usuarioId,
        Long restauranteId,
        Integer diaSemana,
        HorarioRestauranteRequest request
    );
    
    void removerHorario(UUID usuarioId, Long restauranteId, Integer diaSemana);
    
    // Métodos públicos (sem autenticação)
    RestauranteResponse buscarPorSlug(String slug);
    RestauranteResponse buscarPorId(Long restauranteId);
    List<RestauranteResumoResponse> listarRestaurantesAbertos(int limite);
    List<RestauranteResumoResponse> listarRestaurantesAtivos();
    List<RestauranteResumoResponse> buscarPorNome(String nome);
}
