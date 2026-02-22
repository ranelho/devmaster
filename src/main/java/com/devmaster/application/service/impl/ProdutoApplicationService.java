package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.ProdutoService;
import com.devmaster.domain.*;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Produto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProdutoApplicationService implements ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ImagemProdutoRepository imagemProdutoRepository;
    private final GrupoOpcaoRepository grupoOpcaoRepository;
    private final OpcaoRepository opcaoRepository;
    
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
            .disponivel(request.disponivel() != null ? request.disponivel() : true)
            .destaque(request.destaque() != null ? request.destaque() : false)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        produto = produtoRepository.save(produto);
        return ProdutoResponse.from(produto, List.of(), List.of());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarProduto(UUID usuarioId, Long restauranteId, Long produtoId) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        List<ImagemProdutoResponse> imagens = imagemProdutoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(ImagemProdutoResponse::from)
            .collect(Collectors.toList());
        
        List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(grupo -> {
                List<OpcaoResponse> opcoes = opcaoRepository
                    .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                    .stream()
                    .map(OpcaoResponse::from)
                    .collect(Collectors.toList());
                return GrupoOpcaoResponse.from(grupo, opcoes);
            })
            .collect(Collectors.toList());
        
        return ProdutoResponse.from(produto, imagens, grupos);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutos(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        Boolean disponivel,
        Boolean destaque
    ) {
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
            .map(produto -> {
                List<ImagemProdutoResponse> imagens = imagemProdutoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(ImagemProdutoResponse::from)
                    .collect(Collectors.toList());
                
                List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(grupo -> {
                        List<OpcaoResponse> opcoes = opcaoRepository
                            .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                            .stream()
                            .map(OpcaoResponse::from)
                            .collect(Collectors.toList());
                        return GrupoOpcaoResponse.from(grupo, opcoes);
                    })
                    .collect(Collectors.toList());
                
                return ProdutoResumoResponse.from(produto, imagens, grupos);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoResumoResponse> listarProdutosComPaginacao(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        Pageable pageable
    ) {
        buscarRestauranteOuFalhar(restauranteId);
        
        Page<Produto> produtos = categoriaId != null
            ? produtoRepository.findByRestauranteIdAndCategoriaId(restauranteId, categoriaId, pageable)
            : produtoRepository.findByRestauranteId(restauranteId, pageable);
        
        return produtos.map(produto -> {
            List<ImagemProdutoResponse> imagens = imagemProdutoRepository
                .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                .stream()
                .map(ImagemProdutoResponse::from)
                .collect(Collectors.toList());
            
            List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
                .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                .stream()
                .map(grupo -> {
                    List<OpcaoResponse> opcoes = opcaoRepository
                        .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                        .stream()
                        .map(OpcaoResponse::from)
                        .collect(Collectors.toList());
                    return GrupoOpcaoResponse.from(grupo, opcoes);
                })
                .collect(Collectors.toList());
            
            return ProdutoResumoResponse.from(produto, imagens, grupos);
        });
    }
    
    @Override
    @Transactional
    public ProdutoResponse atualizarProduto(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        AtualizarProdutoRequest request
    ) {
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
        
        List<ImagemProdutoResponse> imagens = imagemProdutoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(ImagemProdutoResponse::from)
            .collect(Collectors.toList());
        
        List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(grupo -> {
                List<OpcaoResponse> opcoes = opcaoRepository
                    .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                    .stream()
                    .map(OpcaoResponse::from)
                    .collect(Collectors.toList());
                return GrupoOpcaoResponse.from(grupo, opcoes);
            })
            .collect(Collectors.toList());
        
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
    
    // Imagens
    
    @Override
    @Transactional
    public ImagemProdutoResponse adicionarImagem(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        ImagemProdutoRequest request
    ) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        if (request.imagemBase64() == null && request.urlBucket() == null) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Deve fornecer imagemBase64 ou urlBucket");
        }
        
        boolean isPrincipal = request.principal() != null && request.principal();
        
        if (isPrincipal && imagemProdutoRepository.existsByProdutoIdAndPrincipal(produtoId, true)) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe uma imagem principal para este produto");
        }
        
        ImagemProduto imagem = ImagemProduto.builder()
            .produto(produto)
            .nomeArquivo(request.nomeArquivo())
            .tipoMime(request.tipoMime())
            .tamanhoBytes(request.tamanhoBytes())
            .largura(request.largura())
            .altura(request.altura())
            .imagemBase64(request.imagemBase64())
            .urlBucket(request.urlBucket())
            .principal(isPrincipal)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        imagem = imagemProdutoRepository.save(imagem);
        return ImagemProdutoResponse.from(imagem);
    }
    
    @Override
    @Transactional
    public ImagemProdutoResponse uploadImagem(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        org.springframework.web.multipart.MultipartFile arquivo,
        Boolean principal,
        Integer ordemExibicao
    ) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        // Validar e converter imagem
        com.devmaster.util.ImagemUtil.validarArquivo(arquivo);
        String base64 = com.devmaster.util.ImagemUtil.converterParaBase64(arquivo);
        com.devmaster.util.ImagemUtil.DimensoesImagem dimensoes = 
            com.devmaster.util.ImagemUtil.obterDimensoes(arquivo);
        
        boolean isPrincipal = principal != null && principal;
        
        if (isPrincipal && imagemProdutoRepository.existsByProdutoIdAndPrincipal(produtoId, true)) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe uma imagem principal para este produto");
        }
        
        String nomeArquivo = com.devmaster.util.ImagemUtil.gerarNomeUnico(arquivo.getOriginalFilename());
        
        ImagemProduto imagem = ImagemProduto.builder()
            .produto(produto)
            .nomeArquivo(nomeArquivo)
            .tipoMime(arquivo.getContentType())
            .tamanhoBytes(arquivo.getSize())
            .largura(dimensoes.largura())
            .altura(dimensoes.altura())
            .imagemBase64(base64)
            .urlBucket(null) // Será preenchido quando migrar para bucket
            .principal(isPrincipal)
            .ordemExibicao(ordemExibicao != null ? ordemExibicao : 0)
            .build();
        
        imagem = imagemProdutoRepository.save(imagem);
        return ImagemProdutoResponse.from(imagem);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ImagemProdutoResponse> listarImagens(UUID usuarioId, Long restauranteId, Long produtoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        
        return imagemProdutoRepository.findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(ImagemProdutoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void definirImagemPrincipal(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        
        imagemProdutoRepository.findByProdutoIdAndPrincipal(produtoId, true)
            .ifPresent(img -> {
                img.removerPrincipal();
                imagemProdutoRepository.save(img);
            });
        
        ImagemProduto imagem = imagemProdutoRepository.findById(imagemId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Imagem não encontrada"));
        
        if (!imagem.getProduto().getId().equals(produtoId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Imagem não pertence a este produto");
        }
        
        imagem.definirComoPrincipal();
        imagemProdutoRepository.save(imagem);
    }
    
    @Override
    @Transactional
    public void removerImagem(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        
        ImagemProduto imagem = imagemProdutoRepository.findById(imagemId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Imagem não encontrada"));
        
        if (!imagem.getProduto().getId().equals(produtoId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Imagem não pertence a este produto");
        }
        
        imagemProdutoRepository.delete(imagem);
    }
    
    // Grupos de Opções
    
    @Override
    @Transactional
    public GrupoOpcaoResponse adicionarGrupoOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        GrupoOpcaoRequest request
    ) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        GrupoOpcao grupo = GrupoOpcao.builder()
            .produto(produto)
            .nome(request.nome())
            .descricao(request.descricao())
            .minimoSelecoes(request.minimoSelecoes() != null ? request.minimoSelecoes() : 0)
            .maximoSelecoes(request.maximoSelecoes() != null ? request.maximoSelecoes() : 1)
            .obrigatorio(request.obrigatorio() != null ? request.obrigatorio() : false)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        grupo = grupoOpcaoRepository.save(grupo);
        return GrupoOpcaoResponse.from(grupo, List.of());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<GrupoOpcaoResponse> listarGruposOpcoes(UUID usuarioId, Long restauranteId, Long produtoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        
        return grupoOpcaoRepository.findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(grupo -> {
                List<OpcaoResponse> opcoes = opcaoRepository
                    .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                    .stream()
                    .map(OpcaoResponse::from)
                    .collect(Collectors.toList());
                return GrupoOpcaoResponse.from(grupo, opcoes);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public GrupoOpcaoResponse atualizarGrupoOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        GrupoOpcaoRequest request
    ) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        
        if (request.nome() != null) {
            grupo.setNome(request.nome());
        }
        
        if (request.descricao() != null) {
            grupo.setDescricao(request.descricao());
        }
        
        if (request.minimoSelecoes() != null) {
            grupo.setMinimoSelecoes(request.minimoSelecoes());
        }
        
        if (request.maximoSelecoes() != null) {
            grupo.setMaximoSelecoes(request.maximoSelecoes());
        }
        
        if (request.obrigatorio() != null) {
            grupo.setObrigatorio(request.obrigatorio());
        }
        
        if (request.ordemExibicao() != null) {
            grupo.setOrdemExibicao(request.ordemExibicao());
        }
        
        grupo = grupoOpcaoRepository.save(grupo);
        
        List<OpcaoResponse> opcoes = opcaoRepository
            .findByGrupoOpcaoIdOrderByOrdemExibicao(grupoId)
            .stream()
            .map(OpcaoResponse::from)
            .collect(Collectors.toList());
        
        return GrupoOpcaoResponse.from(grupo, opcoes);
    }
    
    @Override
    @Transactional
    public void removerGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        grupoOpcaoRepository.delete(grupo);
    }
    
    // Opções
    
    @Override
    @Transactional
    public OpcaoResponse adicionarOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        OpcaoRequest request
    ) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        
        Opcao opcao = Opcao.builder()
            .grupoOpcao(grupo)
            .nome(request.nome())
            .precoAdicional(request.precoAdicional() != null ? request.precoAdicional() : java.math.BigDecimal.ZERO)
            .disponivel(request.disponivel() != null ? request.disponivel() : true)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        opcao = opcaoRepository.save(opcao);
        return OpcaoResponse.from(opcao);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OpcaoResponse> listarOpcoes(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Boolean disponivel
    ) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        
        List<Opcao> opcoes = disponivel != null
            ? opcaoRepository.findByGrupoOpcaoIdAndDisponivelOrderByOrdemExibicao(grupoId, disponivel)
            : opcaoRepository.findByGrupoOpcaoIdOrderByOrdemExibicao(grupoId);
        
        return opcoes.stream()
            .map(OpcaoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OpcaoResponse atualizarOpcao(
        UUID usuarioId,
        Long restauranteId,
        Long produtoId,
        Long grupoId,
        Long opcaoId,
        OpcaoRequest request
    ) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        
        if (request.nome() != null) {
            opcao.setNome(request.nome());
        }
        
        if (request.precoAdicional() != null) {
            opcao.setPrecoAdicional(request.precoAdicional());
        }
        
        if (request.disponivel() != null) {
            opcao.setDisponivel(request.disponivel());
        }
        
        if (request.ordemExibicao() != null) {
            opcao.setOrdemExibicao(request.ordemExibicao());
        }
        
        opcao = opcaoRepository.save(opcao);
        return OpcaoResponse.from(opcao);
    }
    
    @Override
    @Transactional
    public void disponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcao.disponibilizar();
        opcaoRepository.save(opcao);
    }
    
    @Override
    @Transactional
    public void indisponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcao.indisponibilizar();
        opcaoRepository.save(opcao);
    }
    
    @Override
    @Transactional
    public void removerOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcaoRepository.delete(opcao);
    }
    
    // Métodos auxiliares
    
    private Restaurante buscarRestauranteOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    }
    
    private Categoria buscarCategoriaOuFalhar(Long restauranteId, Long categoriaId) {
        return categoriaRepository.findByIdAndRestauranteId(categoriaId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
    
    private Produto buscarProdutoOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    
    private GrupoOpcao buscarGrupoOpcaoOuFalhar(Long produtoId, Long grupoId) {
        return grupoOpcaoRepository.findByIdAndProdutoId(grupoId, produtoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo de opção não encontrado"));
    }
    
    private Opcao buscarOpcaoOuFalhar(Long grupoId, Long opcaoId) {
        return opcaoRepository.findByIdAndGrupoOpcaoId(opcaoId, grupoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Opção não encontrada"));
    }
    
    // ========================================
    // MÉTODOS PÚBLICOS (SEM AUTENTICAÇÃO)
    // ========================================
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutosPorRestaurante(
            Long restauranteId,
            Long categoriaId,
            Boolean disponivel,
            Boolean destaque) {
        
        // Validar restaurante existe
        if (!restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado");
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
            .map(produto -> {
                List<ImagemProdutoResponse> imagens = imagemProdutoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(ImagemProdutoResponse::from)
                    .collect(Collectors.toList());
                
                List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(grupo -> {
                        List<OpcaoResponse> opcoes = opcaoRepository
                            .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                            .stream()
                            .map(OpcaoResponse::from)
                            .collect(Collectors.toList());
                        return GrupoOpcaoResponse.from(grupo, opcoes);
                    })
                    .collect(Collectors.toList());
                
                return ProdutoResumoResponse.from(produto, imagens, grupos);
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarProdutoCompleto(Long restauranteId, Long produtoId) {
        Produto produto = produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        
        List<ImagemProdutoResponse> imagens = imagemProdutoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(ImagemProdutoResponse::from)
            .collect(Collectors.toList());
        
        List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
            .findByProdutoIdOrderByOrdemExibicao(produtoId)
            .stream()
            .map(grupo -> {
                List<OpcaoResponse> opcoes = opcaoRepository
                    .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                    .stream()
                    .map(OpcaoResponse::from)
                    .collect(Collectors.toList());
                return GrupoOpcaoResponse.from(grupo, opcoes);
            })
            .collect(Collectors.toList());
        
        return ProdutoResponse.from(produto, imagens, grupos);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite) {
        // Validar restaurante existe
        if (!restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado");
        }
        
        Pageable pageable = PageRequest.of(0, limite);
        return produtoRepository.findByRestauranteIdAndDestaqueTrueAndDisponivelTrueOrderByOrdemExibicaoAsc(
                restauranteId, pageable)
            .stream()
            .map(produto -> {
                List<ImagemProdutoResponse> imagens = imagemProdutoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(ImagemProdutoResponse::from)
                    .collect(Collectors.toList());
                
                List<GrupoOpcaoResponse> grupos = grupoOpcaoRepository
                    .findByProdutoIdOrderByOrdemExibicao(produto.getId())
                    .stream()
                    .map(grupo -> {
                        List<OpcaoResponse> opcoes = opcaoRepository
                            .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                            .stream()
                            .map(OpcaoResponse::from)
                            .collect(Collectors.toList());
                        return GrupoOpcaoResponse.from(grupo, opcoes);
                    })
                    .collect(Collectors.toList());
                
                return ProdutoResumoResponse.from(produto, imagens, grupos);
            })
            .collect(Collectors.toList());
    }
}
