package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.HorarioRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.HorarioRestauranteResponse;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<RestauranteResponse> criarRestaurante(RestauranteRequest request) {
        RestauranteResponse response = restauranteService.criarRestaurante(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<RestauranteResponse> buscarRestaurante(Long restauranteId) {
        RestauranteResponse response = restauranteService.buscarRestaurante(null, restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Page<RestauranteResumoResponse>> listarRestaurantes(String nome, Boolean ativo, Boolean aberto, Pageable pageable) {
        Page<RestauranteResumoResponse> response = restauranteService.listarRestaurantes(null, ativo, aberto, nome, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<RestauranteResponse> atualizarRestaurante(Long restauranteId, AtualizarRestauranteRequest request) {
        RestauranteResponse response = restauranteService.atualizarRestaurante(null, restauranteId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> ativarRestaurante(Long restauranteId) {
        restauranteService.ativarRestaurante(null, restauranteId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> desativarRestaurante(Long restauranteId) {
        restauranteService.desativarRestaurante(null, restauranteId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> abrirRestaurante(Long restauranteId) {
        restauranteService.abrirRestaurante(null, restauranteId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> fecharRestaurante(Long restauranteId) {
        restauranteService.fecharRestaurante(null, restauranteId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<EnderecoRestauranteResponse> buscarEndereco(Long restauranteId) {
        EnderecoRestauranteResponse response = restauranteService.buscarEndereco(null, restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EnderecoRestauranteResponse> adicionarEndereco(Long restauranteId, EnderecoRestauranteRequest request) {
        EnderecoRestauranteResponse response = restauranteService.adicionarEndereco(null, restauranteId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<EnderecoRestauranteResponse> atualizarEndereco(Long restauranteId, EnderecoRestauranteRequest request) {
        EnderecoRestauranteResponse response = restauranteService.atualizarEndereco(null, restauranteId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<HorarioRestauranteResponse>> listarHorarios(Long restauranteId) {
        List<HorarioRestauranteResponse> response = restauranteService.listarHorarios(null, restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<HorarioRestauranteResponse>> atualizarHorarios(Long restauranteId, List<HorarioRestauranteRequest> horarios) {
        List<HorarioRestauranteResponse> response = restauranteService.atualizarHorarios(null, restauranteId, horarios);
        return ResponseEntity.ok(response);
    }
}
