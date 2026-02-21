package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
    public CategoriaResponse criarCategoria(UUID usuarioId, Long restauranteId, CategoriaRequest request) {
        return categoriaService.criarCategoria(usuarioId, restauranteId, request);
    }
    
    @Override
    public CategoriaResponse buscarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        return categoriaService.buscarCategoria(usuarioId, restauranteId, categoriaId);
    }
    
    @Override
    public List<CategoriaResponse> listarCategorias(UUID usuarioId, Long restauranteId, Boolean ativo) {
        return categoriaService.listarCategorias(usuarioId, restauranteId, ativo);
    }
    
    @Override
    public CategoriaResponse atualizarCategoria(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        AtualizarCategoriaRequest request
    ) {
        return categoriaService.atualizarCategoria(usuarioId, restauranteId, categoriaId, request);
    }
    
    @Override
    public void ativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        categoriaService.ativarCategoria(usuarioId, restauranteId, categoriaId);
    }
    
    @Override
    public void desativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        categoriaService.desativarCategoria(usuarioId, restauranteId, categoriaId);
    }
    
    @Override
    public void removerCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        categoriaService.removerCategoria(usuarioId, restauranteId, categoriaId);
    }
}
