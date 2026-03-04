package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para Categoria.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class CategoriaRestController implements CategoriaAPI {
    
    private final CategoriaService categoriaService;
    
    @Override
    public ResponseEntity<CategoriaResponse> criarCategoria(Long restauranteId, CategoriaRequest request) {
        CategoriaResponse response = categoriaService.criarCategoria(null, restauranteId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<CategoriaResponse> buscarCategoria(Long restauranteId, Long categoriaId) {
        CategoriaResponse response = categoriaService.buscarCategoria(null, restauranteId, categoriaId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<CategoriaResponse>> listarCategorias(Long restauranteId, Boolean ativo) {
        List<CategoriaResponse> response = categoriaService.listarCategorias(null, restauranteId, ativo);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<CategoriaResponse> atualizarCategoria(
        Long restauranteId,
        Long categoriaId,
        AtualizarCategoriaRequest request
    ) {
        CategoriaResponse response = categoriaService.atualizarCategoria(null, restauranteId, categoriaId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> ativarCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.ativarCategoria(null, restauranteId, categoriaId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> desativarCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.desativarCategoria(null, restauranteId, categoriaId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.removerCategoria(null, restauranteId, categoriaId);
        return ResponseEntity.noContent().build();
    }
}
