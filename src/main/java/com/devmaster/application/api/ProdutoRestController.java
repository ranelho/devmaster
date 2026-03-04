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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProdutoRestController implements ProdutoAPI {
    
    private final ProdutoService produtoService;
    private final ImagemProdutoService imagemProdutoService;
    private final GrupoOpcaoService grupoOpcaoService;
    private final OpcaoService opcaoService;
    
    @Override
    public ResponseEntity<ProdutoResponse> criarProduto(Long restauranteId, ProdutoRequest request) {
        ProdutoResponse response = produtoService.criarProduto(null, restauranteId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<ProdutoResponse> buscarProduto(Long restauranteId, Long produtoId) {
        ProdutoResponse response = produtoService.buscarProduto(null, restauranteId, produtoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<ProdutoResumoResponse>> listarProdutos(
        Long restauranteId,
        Long categoriaId,
        Boolean disponivel,
        Boolean destaque
    ) {
        List<ProdutoResumoResponse> response = produtoService.listarProdutos(null, restauranteId, categoriaId, disponivel, destaque);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Page<ProdutoResumoResponse>> listarProdutosComPaginacao(
        Long restauranteId,
        Long categoriaId,
        Pageable pageable
    ) {
        Page<ProdutoResumoResponse> response = produtoService.listarProdutosComPaginacao(null, restauranteId, categoriaId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<ProdutoResponse> atualizarProduto(
        Long restauranteId,
        Long produtoId,
        AtualizarProdutoRequest request
    ) {
        ProdutoResponse response = produtoService.atualizarProduto(null, restauranteId, produtoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> disponibilizarProduto(Long restauranteId, Long produtoId) {
        produtoService.disponibilizarProduto(null, restauranteId, produtoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> indisponibilizarProduto(Long restauranteId, Long produtoId) {
        produtoService.indisponibilizarProduto(null, restauranteId, produtoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> destacarProduto(Long restauranteId, Long produtoId) {
        produtoService.destacarProduto(null, restauranteId, produtoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerDestaqueProduto(Long restauranteId, Long produtoId) {
        produtoService.removerDestaqueProduto(null, restauranteId, produtoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerProduto(Long restauranteId, Long produtoId) {
        produtoService.removerProduto(null, restauranteId, produtoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<ImagemProdutoResponse> adicionarImagem(
        Long restauranteId,
        Long produtoId,
        ImagemProdutoRequest request
    ) {
        ImagemProdutoResponse response = imagemProdutoService.adicionarImagem(null, restauranteId, produtoId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<ImagemProdutoResponse> uploadImagem(
        Long restauranteId,
        Long produtoId,
        org.springframework.web.multipart.MultipartFile arquivo,
        Boolean principal,
        Integer ordemExibicao
    ) {
        ImagemProdutoResponse response = imagemProdutoService.uploadImagem(null, restauranteId, produtoId, arquivo, principal, ordemExibicao);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<List<ImagemProdutoResponse>> listarImagens(Long restauranteId, Long produtoId) {
        List<ImagemProdutoResponse> response = imagemProdutoService.listarImagens(null, restauranteId, produtoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> definirImagemPrincipal(Long restauranteId, Long produtoId, Long imagemId) {
        imagemProdutoService.definirImagemPrincipal(null, restauranteId, produtoId, imagemId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerImagem(Long restauranteId, Long produtoId, Long imagemId) {
        imagemProdutoService.removerImagem(null, restauranteId, produtoId, imagemId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<GrupoOpcaoResponse> adicionarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        GrupoOpcaoRequest request
    ) {
        GrupoOpcaoResponse response = grupoOpcaoService.adicionarGrupoOpcao(null, restauranteId, produtoId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<List<GrupoOpcaoResponse>> listarGruposOpcoes(Long restauranteId, Long produtoId) {
        List<GrupoOpcaoResponse> response = grupoOpcaoService.listarGruposOpcoes(null, restauranteId, produtoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<GrupoOpcaoResponse> atualizarGrupoOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        GrupoOpcaoRequest request
    ) {
        GrupoOpcaoResponse response = grupoOpcaoService.atualizarGrupoOpcao(null, restauranteId, produtoId, grupoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> removerGrupoOpcao(Long restauranteId, Long produtoId, Long grupoId) {
        grupoOpcaoService.removerGrupoOpcao(null, restauranteId, produtoId, grupoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<OpcaoResponse> adicionarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        OpcaoRequest request
    ) {
        OpcaoResponse response = opcaoService.adicionarOpcao(null, restauranteId, produtoId, grupoId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<List<OpcaoResponse>> listarOpcoes(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Boolean disponivel
    ) {
        List<OpcaoResponse> response = opcaoService.listarOpcoes(null, restauranteId, produtoId, grupoId, disponivel);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<OpcaoResponse> atualizarOpcao(
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Long opcaoId,
        OpcaoRequest request
    ) {
        OpcaoResponse response = opcaoService.atualizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> disponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.disponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> indisponibilizarOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.indisponibilizarOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerOpcao(Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        opcaoService.removerOpcao(null, restauranteId, produtoId, grupoId, opcaoId);
        return ResponseEntity.noContent().build();
    }
}
