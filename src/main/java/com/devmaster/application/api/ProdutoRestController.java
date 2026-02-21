package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Implementação da API de Produtos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class ProdutoRestController implements ProdutoAPI {
    
    private final ProdutoService produtoService;
    
    @Override
    public ProdutoResponse criarProduto(UUID usuarioId, Long restauranteId, ProdutoRequest request) {
        return produtoService.criarProduto(usuarioId, restauranteId, request);
    }
    
    @Override
    public ProdutoResponse buscarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        return produtoService.buscarProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public List<ProdutoResumoResponse> listarProdutos(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        Boolean disponivel,
        Boolean destaque
    ) {
        return produtoService.listarProdutos(usuarioId, restauranteId, categoriaId, disponivel, destaque);
    }
    
    @Override
    public Page<ProdutoResumoResponse> listarProdutosComPaginacao(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        Pageable pageable
    ) {
        return produtoService.listarProdutosComPaginacao(usuarioId, restauranteId, categoriaId, pageable);
    }
    
    @Override
    public ProdutoResponse atualizarProduto(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        AtualizarProdutoRequest request
    ) {
        return produtoService.atualizarProduto(usuarioId, restauranteId, produtoId, request);
    }
    
    @Override
    public void disponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        produtoService.disponibilizarProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public void indisponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        produtoService.indisponibilizarProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public void destacarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        produtoService.destacarProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public void removerDestaqueProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        produtoService.removerDestaqueProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public void removerProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        produtoService.removerProduto(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public ImagemProdutoResponse adicionarImagem(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        ImagemProdutoRequest request
    ) {
        return produtoService.adicionarImagem(usuarioId, restauranteId, produtoId, request);
    }
    
    @Override
    public ImagemProdutoResponse uploadImagem(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        org.springframework.web.multipart.MultipartFile arquivo,
        Boolean principal,
        Integer ordemExibicao
    ) {
        return produtoService.uploadImagem(usuarioId, restauranteId, produtoId, arquivo, principal, ordemExibicao);
    }
    
    @Override
    public List<ImagemProdutoResponse> listarImagens(UUID usuarioId, Long restauranteId, Long produtoId) {
        return produtoService.listarImagens(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public void definirImagemPrincipal(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId) {
        produtoService.definirImagemPrincipal(usuarioId, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public void removerImagem(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId) {
        produtoService.removerImagem(usuarioId, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public GrupoOpcaoResponse adicionarGrupoOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        GrupoOpcaoRequest request
    ) {
        return produtoService.adicionarGrupoOpcao(usuarioId, restauranteId, produtoId, request);
    }
    
    @Override
    public List<GrupoOpcaoResponse> listarGruposOpcoes(UUID usuarioId, Long restauranteId, Long produtoId) {
        return produtoService.listarGruposOpcoes(usuarioId, restauranteId, produtoId);
    }
    
    @Override
    public GrupoOpcaoResponse atualizarGrupoOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        GrupoOpcaoRequest request
    ) {
        return produtoService.atualizarGrupoOpcao(usuarioId, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public void removerGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId) {
        produtoService.removerGrupoOpcao(usuarioId, restauranteId, produtoId, grupoId);
    }
    
    @Override
    public OpcaoResponse adicionarOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        OpcaoRequest request
    ) {
        return produtoService.adicionarOpcao(usuarioId, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public List<OpcaoResponse> listarOpcoes(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Boolean disponivel
    ) {
        return produtoService.listarOpcoes(usuarioId, restauranteId, produtoId, grupoId, disponivel);
    }
    
    @Override
    public OpcaoResponse atualizarOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Long opcaoId,
        OpcaoRequest request
    ) {
        return produtoService.atualizarOpcao(usuarioId, restauranteId, produtoId, grupoId, opcaoId, request);
    }
    
    @Override
    public void disponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.disponibilizarOpcao(usuarioId, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void indisponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.indisponibilizarOpcao(usuarioId, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void removerOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.removerOpcao(usuarioId, restauranteId, produtoId, grupoId, opcaoId);
    }
}
