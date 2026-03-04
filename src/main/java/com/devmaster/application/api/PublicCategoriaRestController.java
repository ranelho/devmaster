package com.devmaster.application.api;

import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Categorias.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class PublicCategoriaRestController implements PublicCategoriaAPI {
    
    private final CategoriaService categoriaService;
    
    @Override
    public ResponseEntity<List<CategoriaResponse>> listarCategorias(Long restauranteId) {
        List<CategoriaResponse> response = categoriaService.listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<CategoriaResponse> buscarCategoria(Long restauranteId, Long categoriaId) {
        CategoriaResponse response = categoriaService.buscarPorId(categoriaId);
        return ResponseEntity.ok(response);
    }
}
