package com.devmaster.application.service;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Produto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface ProdutoService {
    
    // Produto
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
    
    // Imagens
    ImagemProdutoResponse adicionarImagem(UUID usuarioId, Long restauranteId, Long produtoId, ImagemProdutoRequest request);
    ImagemProdutoResponse uploadImagem(UUID usuarioId, Long restauranteId, Long produtoId, org.springframework.web.multipart.MultipartFile arquivo, Boolean principal, Integer ordemExibicao);
    List<ImagemProdutoResponse> listarImagens(UUID usuarioId, Long restauranteId, Long produtoId);
    void definirImagemPrincipal(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId);
    void removerImagem(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId);
    
    // Grupos de Opções
    GrupoOpcaoResponse adicionarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, GrupoOpcaoRequest request);
    List<GrupoOpcaoResponse> listarGruposOpcoes(UUID usuarioId, Long restauranteId, Long produtoId);
    GrupoOpcaoResponse atualizarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, GrupoOpcaoRequest request);
    void removerGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId);
    
    // Opções
    OpcaoResponse adicionarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, OpcaoRequest request);
    List<OpcaoResponse> listarOpcoes(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Boolean disponivel);
    OpcaoResponse atualizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId, OpcaoRequest request);
    void disponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
    void indisponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
    void removerOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
    
    // Métodos públicos (sem autenticação)
    List<ProdutoResumoResponse> listarProdutosPorRestaurante(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque);
    ProdutoResponse buscarProdutoCompleto(Long restauranteId, Long produtoId);
    List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite);
}
