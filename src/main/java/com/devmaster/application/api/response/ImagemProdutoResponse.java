package com.devmaster.application.api.response;

import com.devmaster.domain.ImagemProduto;

import java.time.LocalDateTime;

public record ImagemProdutoResponse(
    Long id,
    Long produtoId,
    String nomeArquivo,
    String tipoMime,
    Long tamanhoBytes,
    Integer largura,
    Integer altura,
    String imagemBase64,
    String urlBucket,
    Boolean principal,
    Integer ordemExibicao,
    LocalDateTime criadoEm
) {
    public static ImagemProdutoResponse from(ImagemProduto imagem) {
        return new ImagemProdutoResponse(
            imagem.getId(),
            imagem.getProduto().getId(),
            imagem.getNomeArquivo(),
            imagem.getTipoMime(),
            imagem.getTamanhoBytes(),
            imagem.getLargura(),
            imagem.getAltura(),
            imagem.getImagemBase64(),
            imagem.getUrlBucket(),
            imagem.getPrincipal(),
            imagem.getOrdemExibicao(),
            imagem.getCriadoEm()
        );
    }
}
