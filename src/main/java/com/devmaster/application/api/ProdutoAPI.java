package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API para gerenciamento de Produtos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Produtos", description = "Gerenciamento de produtos, imagens e opções")
@RequestMapping({"/v1/restaurantes/{restauranteId}/produtos", "/v2/restaurantes/{restauranteId}/produtos"})
public interface ProdutoAPI {
    
    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto para o restaurante")
    ResponseEntity<ProdutoResponse> criarProduto(
        @PathVariable Long restauranteId,
        @Valid @RequestBody ProdutoRequest request
    );
    
    @GetMapping("/{produtoId}")
    @Operation(summary = "Buscar produto", description = "Busca um produto por ID com imagens e opções")
    ResponseEntity<ProdutoResponse> buscarProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista produtos com filtros opcionais")
    ResponseEntity<List<ProdutoResumoResponse>> listarProdutos(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Boolean disponivel,
        @RequestParam(required = false) Boolean destaque
    );
    
    @GetMapping("/paginado")
    @Operation(summary = "Listar produtos paginado", description = "Lista produtos com paginação")
    ResponseEntity<Page<ProdutoResumoResponse>> listarProdutosComPaginacao(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) Long categoriaId,
        Pageable pageable
    );
    
    @PutMapping("/{produtoId}")
    @Operation(summary = "Atualizar produto", description = "Atualiza dados de um produto")
    ResponseEntity<ProdutoResponse> atualizarProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody AtualizarProdutoRequest request
    );
    
    @PatchMapping("/{produtoId}/disponibilizar")
    @Operation(summary = "Disponibilizar produto", description = "Marca produto como disponível")
    ResponseEntity<Void> disponibilizarProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/indisponibilizar")
    @Operation(summary = "Indisponibilizar produto", description = "Marca produto como indisponível")
    ResponseEntity<Void> indisponibilizarProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/destacar")
    @Operation(summary = "Destacar produto", description = "Marca produto como destaque")
    ResponseEntity<Void> destacarProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/remover-destaque")
    @Operation(summary = "Remover destaque", description = "Remove produto dos destaques")
    ResponseEntity<Void> removerDestaqueProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @DeleteMapping("/{produtoId}")
    @Operation(summary = "Remover produto", description = "Remove um produto")
    ResponseEntity<Void> removerProduto(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    // Imagens
    
    @PostMapping("/{produtoId}/imagens")
    @Operation(summary = "Adicionar imagem", description = "Adiciona uma imagem ao produto (JSON)")
    ResponseEntity<ImagemProdutoResponse> adicionarImagem(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody ImagemProdutoRequest request
    );
    
    @PostMapping(value = "/{produtoId}/imagens/upload", consumes = "multipart/form-data")
    @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem (multipart)")
    ResponseEntity<ImagemProdutoResponse> uploadImagem(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @RequestParam("arquivo") org.springframework.web.multipart.MultipartFile arquivo,
        @RequestParam(required = false, defaultValue = "false") Boolean principal,
        @RequestParam(required = false, defaultValue = "0") Integer ordemExibicao
    );
    
    @GetMapping("/{produtoId}/imagens")
    @Operation(summary = "Listar imagens", description = "Lista todas as imagens do produto")
    ResponseEntity<List<ImagemProdutoResponse>> listarImagens(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/imagens/{imagemId}/principal")
    @Operation(summary = "Definir imagem principal", description = "Define uma imagem como principal")
    ResponseEntity<Void> definirImagemPrincipal(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long imagemId
    );
    
    @DeleteMapping("/{produtoId}/imagens/{imagemId}")
    @Operation(summary = "Remover imagem", description = "Remove uma imagem do produto")
    ResponseEntity<Void> removerImagem(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long imagemId
    );
    
    // Grupos de Opções
    
    @PostMapping("/{produtoId}/grupos-opcoes")
    @Operation(summary = "Adicionar grupo de opções", description = "Adiciona um grupo de opções ao produto")
    ResponseEntity<GrupoOpcaoResponse> adicionarGrupoOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody GrupoOpcaoRequest request
    );
    
    @GetMapping("/{produtoId}/grupos-opcoes")
    @Operation(summary = "Listar grupos de opções", description = "Lista todos os grupos de opções do produto")
    ResponseEntity<List<GrupoOpcaoResponse>> listarGruposOpcoes(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PutMapping("/{produtoId}/grupos-opcoes/{grupoId}")
    @Operation(summary = "Atualizar grupo de opções", description = "Atualiza um grupo de opções")
    ResponseEntity<GrupoOpcaoResponse> atualizarGrupoOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @Valid @RequestBody GrupoOpcaoRequest request
    );
    
    @DeleteMapping("/{produtoId}/grupos-opcoes/{grupoId}")
    @Operation(summary = "Remover grupo de opções", description = "Remove um grupo de opções")
    ResponseEntity<Void> removerGrupoOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId
    );
    
    // Opções
    
    @PostMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes")
    @Operation(summary = "Adicionar opção", description = "Adiciona uma opção ao grupo")
    ResponseEntity<OpcaoResponse> adicionarOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @Valid @RequestBody OpcaoRequest request
    );
    
    @GetMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes")
    @Operation(summary = "Listar opções", description = "Lista todas as opções de um grupo")
    ResponseEntity<List<OpcaoResponse>> listarOpcoes(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @RequestParam(required = false) Boolean disponivel
    );
    
    @PutMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}")
    @Operation(summary = "Atualizar opção", description = "Atualiza uma opção")
    ResponseEntity<OpcaoResponse> atualizarOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId,
        @Valid @RequestBody OpcaoRequest request
    );
    
    @PatchMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}/disponibilizar")
    @Operation(summary = "Disponibilizar opção", description = "Marca opção como disponível")
    ResponseEntity<Void> disponibilizarOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
    
    @PatchMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}/indisponibilizar")
    @Operation(summary = "Indisponibilizar opção", description = "Marca opção como indisponível")
    ResponseEntity<Void> indisponibilizarOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
    
    @DeleteMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}")
    @Operation(summary = "Remover opção", description = "Remove uma opção do grupo")
    ResponseEntity<Void> removerOpcao(
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
}
