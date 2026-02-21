package com.devmaster.application.api;

import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Categorias.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicCategoriaRestController implements PublicCategoriaAPI {
    
    private final CategoriaService categoriaService;
    
    @Override
    public List<CategoriaResponse> listarCategorias(Long restauranteId) {
        log.info("Listando categorias públicas do restaurante: {}", restauranteId);
        return categoriaService.listarPorRestaurante(restauranteId);
    }
    
    @Override
    public CategoriaResponse buscarCategoria(Long restauranteId, Long categoriaId) {
        log.info("Buscando categoria pública: {} do restaurante: {}", categoriaId, restauranteId);
        return categoriaService.buscarPorId(categoriaId);
    }
}
