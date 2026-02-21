package com.devmaster.application.service;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Categoria.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface CategoriaService {
    
    CategoriaResponse criarCategoria(UUID usuarioId, Long restauranteId, CategoriaRequest request);
    
    CategoriaResponse buscarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId);
    
    List<CategoriaResponse> listarCategorias(UUID usuarioId, Long restauranteId, Boolean ativo);
    
    Page<CategoriaResponse> listarCategoriasComPaginacao(
        UUID usuarioId,
        Long restauranteId,
        Pageable pageable
    );
    
    CategoriaResponse atualizarCategoria(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        AtualizarCategoriaRequest request
    );
    
    void ativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId);
    
    void desativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId);
    
    void removerCategoria(UUID usuarioId, Long restauranteId, Long categoriaId);
    
    // Métodos públicos (sem autenticação)
    List<CategoriaResponse> listarPorRestaurante(Long restauranteId);
    CategoriaResponse buscarPorId(Long categoriaId);
}
