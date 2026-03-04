package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class PublicRestauranteRestController implements PublicRestauranteAPI {
    
    private final RestauranteService restauranteService;
    
    @Override
    public ResponseEntity<RestauranteResponse> buscarRestaurantePorSlug(String slug) {
        RestauranteResponse response = restauranteService.buscarPorSlug(slug);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<RestauranteResponse> buscarRestaurante(Long restauranteId) {
        RestauranteResponse response = restauranteService.buscarPorId(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<RestauranteResumoResponse>> listarRestaurantesAbertos(int limite) {
        List<RestauranteResumoResponse> response = restauranteService.listarRestaurantesAbertos(limite);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<RestauranteResumoResponse>> listarRestaurantesAtivos() {
        List<RestauranteResumoResponse> response = restauranteService.listarRestaurantesAtivos();
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<RestauranteResumoResponse>> buscarPorNome(String nome) {
        List<RestauranteResumoResponse> response = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(response);
    }
}
