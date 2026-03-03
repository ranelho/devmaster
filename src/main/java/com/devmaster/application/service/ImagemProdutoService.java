package com.devmaster.application.service;

import com.devmaster.application.api.request.ImagemProdutoRequest;
import com.devmaster.application.api.response.ImagemProdutoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ImagemProdutoService {
    ImagemProdutoResponse adicionarImagem(UUID usuarioId, Long restauranteId, Long produtoId, ImagemProdutoRequest request);
    ImagemProdutoResponse uploadImagem(UUID usuarioId, Long restauranteId, Long produtoId, MultipartFile arquivo, Boolean principal, Integer ordemExibicao);
    List<ImagemProdutoResponse> listarImagens(UUID usuarioId, Long restauranteId, Long produtoId);
    void definirImagemPrincipal(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId);
    void removerImagem(UUID usuarioId, Long restauranteId, Long produtoId, Long imagemId);
}
