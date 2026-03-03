package com.devmaster.application.service;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Produto (CRUD básico).
 * Responsabilidades de Imagens, Grupos e Opções foram movidas para serviços específicos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface ProdutoService {
    
    ProdutoResponse criarProduto(UUID usuarioId, Long restauranteId, ProdutoRequest request);
    ProdutoResponse buscarProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    List<ProdutoResumoResponse> listarProdutos(UUID usuarioId, Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque);
    Page<ProdutoResumoResponse> listarProdutosComPaginacao(UUID usuarioId, Long restauranteId, Long categoriaId, Pageable pageable);
    ProdutoResponse atualizarProduto(UUID usuarioId, Long restauranteId, Long produtoId, AtualizarProdutoRequest request);
    void disponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    void indisponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    void destacarProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    void removerDestaqueProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    void removerProduto(UUID usuarioId, Long restauranteId, Long produtoId);
    
    // Métodos públicos (sem autenticação)
    List<ProdutoResumoResponse> listarProdutosPorRestaurante(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque);
    ProdutoResponse buscarProdutoCompleto(Long restauranteId, Long produtoId);
    List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite);
}
