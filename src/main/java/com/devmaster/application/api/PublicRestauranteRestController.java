package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import lombok.RequiredArgsConstructor;
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
    public RestauranteResponse buscarRestaurantePorSlug(String slug) {
        return restauranteService.buscarPorSlug(slug);
    }
    
    @Override
    public RestauranteResponse buscarRestaurante(Long restauranteId) {
        return restauranteService.buscarPorId(restauranteId);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAbertos(int limite) {
        return restauranteService.listarRestaurantesAbertos(limite);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAtivos() {
        return restauranteService.listarRestaurantesAtivos();
    }
    
    @Override
    public List<RestauranteResumoResponse> buscarPorNome(String nome) {
        return restauranteService.buscarPorNome(nome);
    }
}
