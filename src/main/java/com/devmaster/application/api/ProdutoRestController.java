package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ProdutoResponse criarProduto(Long restauranteId, ProdutoRequest request) {
        return produtoService.criarProduto(null, restauranteId, request);
    }
    
    @Override
    public ProdutoResponse buscarProduto(Long restauranteId, Long produtoId) {
        return produtoService.buscarProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public List<ProdutoResumoResponse> listarProdutos(
        Long restauranteId,
        Long categoriaId,
        Boolean disponivel,
        Boolean destaque
    ) {
        return produtoService.listarProdutos(null, restauranteId, categoriaId, disponivel, destaque);
    }
    
    @Override
    public Page<ProdutoResumoResponse> listarProdutosComPaginacao(
        Long restauranteId,
        Long categoriaId,
        Pageable pageable
    ) {
        return produtoService.listarProdutosComPaginacao(null, restauranteId, categoriaId, pageable);
    }
    
    @Override
    public ProdutoResponse atualizarProduto(
        Long restauranteId,
        Long produtoId,
        AtualizarProdutoRequest request
    ) {
        return produtoService.atualizarProduto(null, restauranteId, produtoId, request);
    }
    
    @Override
    public void disponibilizarProduto(Long restauranteId, Long produtoId) {
        produtoService.disponibilizarProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public void indisponibilizarProduto(Long restauranteId, Long produtoId) {
        produtoService.indisponibilizarProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public void destacarProduto(Long restauranteId, Long produtoId) {
        produtoService.destacarProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public void removerDestaqueProduto(Long restauranteId, Long produtoId) {
        produtoService.removerDestaqueProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public void removerProduto(Long restauranteId, Long produtoId) {
        produtoService.removerProduto(null, restauranteId, produtoId);
    }
    
    @Override
    public ImagemProdutoResponse adicionarImagem(
        Long restauranteId,
        Long produtoId,
        ImagemProdutoRequest request
    ) {
        return produtoService.adicionarImagem(null, restauranteId, produtoId, request);
    }
    
    @Override
    public ImagemProdutoResponse uploadImagem(
        Long restauranteId,
        Long produtoId,
        org.springframework.web.multipart.MultipartFile arquivo,
        Boolean principal,
        Integer ordemExibicao
    ) {
        return produtoService.uploadImagem(null, restauranteId, produtoId, arquivo, principal, ordemExibicao);
    }
    
    @Override
    public List<ImagemProdutoResponse> listarImagens(Long restauranteId, Long produtoId) {
        return produtoService.listarImagens(null, restauranteId, produtoId);
    }
    
    @Override
    public void definirImagemPrincipal(Long restauranteId, Long produtoId, Long imagemId) {
        produtoService.definirImagemPrincipal(null, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public void removerImagem(Long restauranteId, Long produtoId, Long imagemId) {
        produtoService.removerImagem(null, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public GrupoOpcaoResponse adicionarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        GrupoOpcaoRequest request
    ) {
        return produtoService.adicionarGrupoOpcao(null, restauranteId, produtoId, request);
    }
    
    @Override
    public List<GrupoOpcaoResponse> listarGruposOpcoes(Long restauranteId, Long produtoId) {
        return produtoService.listarGruposOpcoes(null, restauranteId, produtoId);
    }
    
    @Override
    public GrupoOpcaoResponse atualizarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        GrupoOpcaoRequest request
    ) {
        return produtoService.atualizarGrupoOpcao(null, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public void removerGrupoOpcao(Long restauranteId, Long produtoId, Long grupoId) {
        produtoService.removerGrupoOpcao(null, restauranteId, produtoId, grupoId);
    }
    
    @Override
    public OpcaoResponse adicionarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        OpcaoRequest request
    ) {
        return produtoService.adicionarOpcao(null, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public List<OpcaoResponse> listarOpcoes(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Boolean disponivel
    ) {
        return produtoService.listarOpcoes(null, restauranteId, produtoId, grupoId, disponivel);
    }
    
    @Override
    public OpcaoResponse atualizarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Long opcaoId,
        OpcaoRequest request
    ) {
        return produtoService.atualizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId, request);
    }
    
    @Override
    public void disponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.disponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void indisponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.indisponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void removerOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        produtoService.removerOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
}
