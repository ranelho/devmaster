package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.ProdutoService;
import com.devmaster.application.service.ImagemProdutoService;
import com.devmaster.application.service.GrupoOpcaoService;
import com.devmaster.application.service.OpcaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProdutoRestController implements ProdutoAPI {
    
    private final ProdutoService produtoService;
    private final ImagemProdutoService imagemProdutoService;
    private final GrupoOpcaoService grupoOpcaoService;
    private final OpcaoService opcaoService;
    
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
        return imagemProdutoService.adicionarImagem(null, restauranteId, produtoId, request);
    }
    
    @Override
    public ImagemProdutoResponse uploadImagem(
        Long restauranteId,
        Long produtoId,
        org.springframework.web.multipart.MultipartFile arquivo,
        Boolean principal,
        Integer ordemExibicao
    ) {
        return imagemProdutoService.uploadImagem(null, restauranteId, produtoId, arquivo, principal, ordemExibicao);
    }
    
    @Override
    public List<ImagemProdutoResponse> listarImagens(Long restauranteId, Long produtoId) {
        return imagemProdutoService.listarImagens(null, restauranteId, produtoId);
    }
    
    @Override
    public void definirImagemPrincipal(Long restauranteId, Long produtoId, Long imagemId) {
        imagemProdutoService.definirImagemPrincipal(null, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public void removerImagem(Long restauranteId, Long produtoId, Long imagemId) {
        imagemProdutoService.removerImagem(null, restauranteId, produtoId, imagemId);
    }
    
    @Override
    public GrupoOpcaoResponse adicionarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        GrupoOpcaoRequest request
    ) {
        return grupoOpcaoService.adicionarGrupoOpcao(null, restauranteId, produtoId, request);
    }
    
    @Override
    public List<GrupoOpcaoResponse> listarGruposOpcoes(Long restauranteId, Long produtoId) {
        return grupoOpcaoService.listarGruposOpcoes(null, restauranteId, produtoId);
    }
    
    @Override
    public GrupoOpcaoResponse atualizarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        GrupoOpcaoRequest request
    ) {
        return grupoOpcaoService.atualizarGrupoOpcao(null, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public void removerGrupoOpcao(Long restauranteId, Long produtoId, Long grupoId) {
        grupoOpcaoService.removerGrupoOpcao(null, restauranteId, produtoId, grupoId);
    }
    
    @Override
    public OpcaoResponse adicionarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        OpcaoRequest request
    ) {
        return opcaoService.adicionarOpcao(null, restauranteId, produtoId, grupoId, request);
    }
    
    @Override
    public List<OpcaoResponse> listarOpcoes(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Boolean disponivel
    ) {
        return opcaoService.listarOpcoes(null, restauranteId, produtoId, grupoId, disponivel);
    }
    
    @Override
    public OpcaoResponse atualizarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Long opcaoId,
        OpcaoRequest request
    ) {
        return opcaoService.atualizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId, request);
    }
    
    @Override
    public void disponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.disponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void indisponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.indisponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
    
    @Override
    public void removerOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.removerOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
    }
}
