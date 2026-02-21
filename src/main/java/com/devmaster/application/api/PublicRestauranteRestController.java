package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicRestauranteRestController implements PublicRestauranteAPI {
    
    private final RestauranteService restauranteService;
    
    @Override
    public RestauranteResponse buscarRestaurantePorSlug(String slug) {
        log.info("Buscando restaurante público por slug: {}", slug);
        return restauranteService.buscarPorSlug(slug);
    }
    
    @Override
    public RestauranteResponse buscarRestaurante(Long restauranteId) {
        log.info("Buscando restaurante público por ID: {}", restauranteId);
        return restauranteService.buscarPorId(restauranteId);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAbertos(int limite) {
        log.info("Listando restaurantes abertos (limite: {})", limite);
        return restauranteService.listarRestaurantesAbertos(limite);
    }
    
    @Override
    public List<RestauranteResumoResponse> listarRestaurantesAtivos() {
        log.info("Listando todos restaurantes ativos");
        return restauranteService.listarRestaurantesAtivos();
    }
    
    @Override
    public List<RestauranteResumoResponse> buscarPorNome(String nome) {
        log.info("Buscando restaurantes por nome: {}", nome);
        return restauranteService.buscarPorNome(nome);
    }
}
