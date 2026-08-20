package com.devmaster.application.api.response;

import com.devmaster.domain.ImagemProduto;

public record ImagemProdutoResponse (
    Long produtoId,
    Long imagemId,
    String nome,
    Boolean principal,
    Integer ordemExibicao
) {
    public ImagemProdutoResponse(ImagemProduto imagemProduto) {
        this(imagemProduto.getProduto().getId(),
                imagemProduto.getId(),
                imagemProduto.getNomeArquivo(),
                imagemProduto.getPrincipal(),
                imagemProduto.getOrdemExibicao()
        );
    }
}
