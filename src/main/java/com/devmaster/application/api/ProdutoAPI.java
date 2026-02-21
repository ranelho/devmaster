package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API para gerenciamento de Produtos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Produtos", description = "Gerenciamento de produtos, imagens e opções")
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos")
public interface ProdutoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar produto", description = "Cria um novo produto para o restaurante")
    ProdutoResponse criarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @Valid @RequestBody ProdutoRequest request
    );
    
    @GetMapping("/{produtoId}")
    @Operation(summary = "Buscar produto", description = "Busca um produto por ID com imagens e opções")
    ProdutoResponse buscarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista produtos com filtros opcionais")
    List<ProdutoResumoResponse> listarProdutos(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) Boolean disponivel,
        @RequestParam(required = false) Boolean destaque
    );
    
    @GetMapping("/paginado")
    @Operation(summary = "Listar produtos paginado", description = "Lista produtos com paginação")
    Page<ProdutoResumoResponse> listarProdutosComPaginacao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @RequestParam(required = false) Long categoriaId,
        Pageable pageable
    );
    
    @PutMapping("/{produtoId}")
    @Operation(summary = "Atualizar produto", description = "Atualiza dados de um produto")
    ProdutoResponse atualizarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody AtualizarProdutoRequest request
    );
    
    @PatchMapping("/{produtoId}/disponibilizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disponibilizar produto", description = "Marca produto como disponível")
    void disponibilizarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/indisponibilizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Indisponibilizar produto", description = "Marca produto como indisponível")
    void indisponibilizarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/destacar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Destacar produto", description = "Marca produto como destaque")
    void destacarProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/remover-destaque")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover destaque", description = "Remove produto dos destaques")
    void removerDestaqueProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover produto", description = "Remove um produto")
    void removerProduto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    // Imagens
    
    @PostMapping("/{produtoId}/imagens")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar imagem", description = "Adiciona uma imagem ao produto (JSON)")
    ImagemProdutoResponse adicionarImagem(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody ImagemProdutoRequest request
    );
    
    @PostMapping(value = "/{produtoId}/imagens/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem (multipart)")
    ImagemProdutoResponse uploadImagem(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @RequestParam("arquivo") org.springframework.web.multipart.MultipartFile arquivo,
        @RequestParam(required = false, defaultValue = "false") Boolean principal,
        @RequestParam(required = false, defaultValue = "0") Integer ordemExibicao
    );
    
    @GetMapping("/{produtoId}/imagens")
    @Operation(summary = "Listar imagens", description = "Lista todas as imagens do produto")
    List<ImagemProdutoResponse> listarImagens(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PatchMapping("/{produtoId}/imagens/{imagemId}/principal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Definir imagem principal", description = "Define uma imagem como principal")
    void definirImagemPrincipal(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long imagemId
    );
    
    @DeleteMapping("/{produtoId}/imagens/{imagemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover imagem", description = "Remove uma imagem do produto")
    void removerImagem(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long imagemId
    );
    
    // Grupos de Opções
    
    @PostMapping("/{produtoId}/grupos-opcoes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar grupo de opções", description = "Adiciona um grupo de opções ao produto")
    GrupoOpcaoResponse adicionarGrupoOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @Valid @RequestBody GrupoOpcaoRequest request
    );
    
    @GetMapping("/{produtoId}/grupos-opcoes")
    @Operation(summary = "Listar grupos de opções", description = "Lista todos os grupos de opções do produto")
    List<GrupoOpcaoResponse> listarGruposOpcoes(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId
    );
    
    @PutMapping("/{produtoId}/grupos-opcoes/{grupoId}")
    @Operation(summary = "Atualizar grupo de opções", description = "Atualiza um grupo de opções")
    GrupoOpcaoResponse atualizarGrupoOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @Valid @RequestBody GrupoOpcaoRequest request
    );
    
    @DeleteMapping("/{produtoId}/grupos-opcoes/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover grupo de opções", description = "Remove um grupo de opções")
    void removerGrupoOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId
    );
    
    // Opções
    
    @PostMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar opção", description = "Adiciona uma opção ao grupo")
    OpcaoResponse adicionarOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @Valid @RequestBody OpcaoRequest request
    );
    
    @GetMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes")
    @Operation(summary = "Listar opções", description = "Lista todas as opções do grupo")
    List<OpcaoResponse> listarOpcoes(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @RequestParam(required = false) Boolean disponivel
    );
    
    @PutMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}")
    @Operation(summary = "Atualizar opção", description = "Atualiza uma opção")
    OpcaoResponse atualizarOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId,
        @Valid @RequestBody OpcaoRequest request
    );
    
    @PatchMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}/disponibilizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Disponibilizar opção", description = "Marca opção como disponível")
    void disponibilizarOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
    
    @PatchMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}/indisponibilizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Indisponibilizar opção", description = "Marca opção como indisponível")
    void indisponibilizarOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
    
    @DeleteMapping("/{produtoId}/grupos-opcoes/{grupoId}/opcoes/{opcaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover opção", description = "Remove uma opção")
    void removerOpcao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @PathVariable Long produtoId,
        @PathVariable Long grupoId,
        @PathVariable Long opcaoId
    );
}
