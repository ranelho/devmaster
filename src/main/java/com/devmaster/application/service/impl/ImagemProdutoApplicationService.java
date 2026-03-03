package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.ImagemProdutoRequest;
import com.devmaster.application.api.response.ImagemProdutoResponse;
import com.devmaster.application.service.ImagemProdutoService;
import com.devmaster.domain.ImagemProduto;
import com.devmaster.domain.Produto;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.ImagemProdutoRepository;
import com.devmaster.infrastructure.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagemProdutoApplicationService implements ImagemProdutoService {
    
    private final ImagemProdutoRepository imagemProdutoRepository;
    private final ProdutoRepository produtoRepository;
    
    @Override
    @Transactional
    public ImagemProdutoResponse adicionarImagem(UUID usuarioId, Long restauranteId, Long produtoId, ImagemProdutoRequest request) {
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
    public ImagemProdutoResponse uploadImagem(UUID usuarioId, Long restauranteId, Long produtoId, MultipartFile arquivo, Boolean principal, Integer ordemExibicao) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);
        
        com.devmaster.util.ImagemUtil.validarArquivo(arquivo);
        String base64 = com.devmaster.util.ImagemUtil.converterParaBase64(arquivo);
        com.devmaster.util.ImagemUtil.DimensoesImagem dimensoes = com.devmaster.util.ImagemUtil.obterDimensoes(arquivo);
        
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
            .urlBucket(null)
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
            .toList();
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
    
    private Produto buscarProdutoOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
}
