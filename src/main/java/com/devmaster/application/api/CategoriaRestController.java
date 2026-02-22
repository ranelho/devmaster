package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
    public CategoriaResponse criarCategoria(Long restauranteId, CategoriaRequest request) {
        return categoriaService.criarCategoria(null, restauranteId, request);
    }
    
    @Override
    public CategoriaResponse buscarCategoria(Long restauranteId, Long categoriaId) {
        return categoriaService.buscarCategoria(null, restauranteId, categoriaId);
    }
    
    @Override
    public List<CategoriaResponse> listarCategorias(Long restauranteId, Boolean ativo) {
        return categoriaService.listarCategorias(null, restauranteId, ativo);
    }
    
    @Override
    public CategoriaResponse atualizarCategoria(
        Long restauranteId,
        Long categoriaId,
        AtualizarCategoriaRequest request
    ) {
        return categoriaService.atualizarCategoria(null, restauranteId, categoriaId, request);
    }
    
    @Override
    public void ativarCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.ativarCategoria(null, restauranteId, categoriaId);
    }
    
    @Override
    public void desativarCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.desativarCategoria(null, restauranteId, categoriaId);
    }
    
    @Override
    public void removerCategoria(Long restauranteId, Long categoriaId) {
        categoriaService.removerCategoria(null, restauranteId, categoriaId);
    }
}
