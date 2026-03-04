package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.ProdutoService;
import com.devmaster.application.service.ImagemProdutoService;
import com.devmaster.application.service.GrupoOpcaoService;
import com.devmaster.application.service.DescontoService;
import com.devmaster.domain.*;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implementação refatorada do serviço de Produto.
 * Focado apenas em operações de produtos, delegando imagens, grupos e opções para serviços especializados.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoApplicationService implements ProdutoService {

    public static final String RESTAURANTE_NAO_ENCONTRADO = "Restaurante não encontrado";
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ImagemProdutoService imagemProdutoService;
    private final GrupoOpcaoService grupoOpcaoService;
    private final DescontoService descontoService;
    
    @Override
    @Transactional
    public ProdutoResponse criarProduto(UUID usuarioId, Long restauranteId, ProdutoRequest request) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, request.categoriaId());
        
        if (produtoRepository.existsByRestauranteIdAndNome(restauranteId, request.nome())) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe um produto com este nome");
        }
        
        Produto produto = Produto.builder()
            .restaurante(restaurante)
            .categoria(categoria)
            .nome(request.nome())
            .descricao(request.descricao())
            .preco(request.preco())
            .precoPromocional(request.precoPromocional())
            .tempoPreparo(request.tempoPreparo())
            .disponivel(request.disponivel() != null && request.disponivel())
            .destaque(request.destaque() != null && request.destaque())
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        produto = produtoRepository.save(produto);
        return ProdutoResponse.from(produto, List.of(), List.of());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        List<ImagemProdutoResponse> imagens = imagemProdutoService.listarImagens(usuarioId, restauranteId, produtoId);
        List<GrupoOpcaoResponse> grupos = grupoOpcaoService.listarGruposOpcoes(usuarioId, restauranteId, produtoId);
        return ProdutoResponse.from(produto, imagens, grupos);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutos(UUID usuarioId, Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque) {
        buscarRestauranteOuFalhar(restauranteId);
        
        List<Produto> produtos;
        
        if (destaque != null && destaque) {
            produtos = produtoRepository.findByRestauranteIdAndDestaqueOrderByOrdemExibicao(restauranteId, true);
        } else if (categoriaId != null) {
            produtos = produtoRepository.findByRestauranteIdAndCategoriaIdOrderByOrdemExibicao(restauranteId, categoriaId);
        } else if (disponivel != null) {
            produtos = produtoRepository.findByRestauranteIdAndDisponivelOrderByOrdemExibicao(restauranteId, disponivel);
        } else {
            produtos = produtoRepository.findByRestauranteIdOrderByOrdemExibicao(restauranteId);
        }
        
        return produtos.stream()
            .map(produto -> construirProdutoResumoResponse(usuarioId, restauranteId, produto))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> listarProdutosComPaginacao(UUID usuarioId, Long restauranteId, Long categoriaId, Pageable pageable) {
        buscarRestauranteOuFalhar(restauranteId);
        
        Page<Produto> produtos = categoriaId != null
            ? produtoRepository.findByRestauranteIdAndCategoriaId(restauranteId, categoriaId, pageable)
            : produtoRepository.findByRestauranteId(restauranteId, pageable);
        
        return produtos.map(produto -> construirProdutoResumoResponse(usuarioId, restauranteId, produto));
    }
    
    @Override
    @Transactional
    public ProdutoResponse atualizarProduto(UUID usuarioId, Long restauranteId, Long produtoId, AtualizarProdutoRequest request) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        if (request.categoriaId() != null) {
            Categoria categoria = buscarCategoriaOuFalhar(restauranteId, request.categoriaId());
            produto.setCategoria(categoria);
        }
        
        if (request.nome() != null) {
            if (!request.nome().equals(produto.getNome()) &&
                produtoRepository.existsByRestauranteIdAndNome(restauranteId, request.nome())) {
                throw APIException.build(HttpStatus.CONFLICT, "Já existe um produto com este nome");
            }
            produto.setNome(request.nome());
        }
        
        if (request.descricao() != null) {
            produto.setDescricao(request.descricao());
        }
        
        if (request.preco() != null) {
            produto.setPreco(request.preco());
        }
        
        if (request.precoPromocional() != null) {
            produto.setPrecoPromocional(request.precoPromocional());
        }
        
        if (request.tempoPreparo() != null) {
            produto.setTempoPreparo(request.tempoPreparo());
        }
        
        if (request.ordemExibicao() != null) {
            produto.setOrdemExibicao(request.ordemExibicao());
        }
        
        produto = produtoRepository.save(produto);
        List<ImagemProdutoResponse> imagens = imagemProdutoService.listarImagens(usuarioId, restauranteId, produtoId);
        List<GrupoOpcaoResponse> grupos = grupoOpcaoService.listarGruposOpcoes(usuarioId, restauranteId, produtoId);
        return ProdutoResponse.from(produto, imagens, grupos);
    }
    
    @Override
    @Transactional
    public void disponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        produto.disponibilizar();
        produtoRepository.save(produto);
    }
    
    @Override
    @Transactional
    public void indisponibilizarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        produto.indisponibilizar();
        produtoRepository.save(produto);
    }
    
    @Override
    @Transactional
    public void destacarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        produto.destacar();
        produtoRepository.save(produto);
    }
    
    @Override
    @Transactional
    public void removerDestaqueProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        produto.removerDestaque();
        produtoRepository.save(produto);
    }
    
    @Override
    @Transactional
    public void removerProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        produtoRepository.delete(produto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutosPorRestaurante(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, RESTAURANTE_NAO_ENCONTRADO);
        }
        
        List<Produto> produtos;
        
        if (categoriaId != null && disponivel != null && destaque != null) {
            produtos = produtoRepository.findByRestauranteIdAndCategoriaIdAndDisponivelAndDestaque(
                restauranteId, categoriaId, disponivel, destaque);
        } else if (categoriaId != null && disponivel != null) {
            produtos = produtoRepository.findByRestauranteIdAndCategoriaIdAndDisponivel(
                restauranteId, categoriaId, disponivel);
        } else if (categoriaId != null) {
            produtos = produtoRepository.findByRestauranteIdAndCategoriaId(restauranteId, categoriaId);
        } else if (disponivel != null && destaque != null) {
            produtos = produtoRepository.findByRestauranteIdAndDisponivelAndDestaque(
                restauranteId, disponivel, destaque);
        } else if (disponivel != null) {
            produtos = produtoRepository.findByRestauranteIdAndDisponivel(restauranteId, disponivel);
        } else if (destaque != null) {
            produtos = produtoRepository.findByRestauranteIdAndDestaque(restauranteId, destaque);
        } else {
            produtos = produtoRepository.findByRestauranteIdOrderByOrdemExibicaoAsc(restauranteId);
        }
        
        return produtos.stream()
            .map(produto -> construirProdutoResumoResponse(null, restauranteId, produto))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarProdutoCompleto(Long restauranteId, Long produtoId) {
        Produto produto = produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        List<ImagemProdutoResponse> imagens = imagemProdutoService.listarImagens(null, restauranteId, produtoId);
        List<GrupoOpcaoResponse> grupos = grupoOpcaoService.listarGruposOpcoes(null, restauranteId, produtoId);
        return ProdutoResponse.from(produto, imagens, grupos);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, RESTAURANTE_NAO_ENCONTRADO);
        }
        
        Pageable pageable = PageRequest.of(0, limite);
        return produtoRepository.findByRestauranteIdAndDestaqueTrueAndDisponivelTrueOrderByOrdemExibicaoAsc(
                restauranteId, pageable)
            .stream()
            .map(produto -> construirProdutoResumoResponse(null, restauranteId, produto))
            .toList();
    }
    
    private Restaurante buscarRestauranteOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, RESTAURANTE_NAO_ENCONTRADO));
    }
    
    private Categoria buscarCategoriaOuFalhar(Long restauranteId, Long categoriaId) {
        return categoriaRepository.findByIdAndRestauranteId(categoriaId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
    
    private Produto buscarProdutoOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    
    private ProdutoResumoResponse construirProdutoResumoResponse(UUID usuarioId, Long restauranteId, Produto produto) {
        List<ImagemProdutoResponse> imagens = imagemProdutoService.listarImagens(usuarioId, restauranteId, produto.getId());
        List<GrupoOpcaoResponse> grupos = grupoOpcaoService.listarGruposOpcoes(usuarioId, restauranteId, produto.getId());
        
        List<DescontoResponse> descontos = descontoService.listarPorProduto(usuarioId, produto.getId());
        log.debug("📊 Produto ID={}: {} descontos encontrados", produto.getId(), descontos.size());
        
        DescontoResponse descontoVigente = descontos.isEmpty() ? null : descontos.stream()
            .filter(d -> Boolean.TRUE.equals(d.ativo()) && Boolean.TRUE.equals(d.vigente()))
            .findFirst()
            .orElse(null);
        
        BigDecimal percentualDesconto = descontoVigente != null ? descontoVigente.percentualDesconto() : null;
        BigDecimal valorOriginal = descontoVigente != null ? produto.getPreco() : null;
        BigDecimal valorComDesconto = descontoVigente != null ? produto.getPreco().subtract(
            produto.getPreco().multiply(percentualDesconto).divide(BigDecimal.valueOf(100))
        ) : null;
        Boolean temDescontoVigente = descontoVigente != null;
        
        log.debug("🏷️ Produto ID={}: desconto={}%, valorOriginal={}, valorComDesconto={}, vigente={}",
            produto.getId(), percentualDesconto, valorOriginal, valorComDesconto, temDescontoVigente);
        
        return ProdutoResumoResponse.from(produto, imagens, grupos, percentualDesconto, valorOriginal, valorComDesconto, temDescontoVigente);
    }
}
