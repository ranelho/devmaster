package com.devmaster.application.api.response;

import com.devmaster.domain.Produto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        Long categoryId,
        Long restauranteId,
        String nome,
        String descricao,
        BigDecimal preco,
        BigDecimal precoPromocional,
        Integer tempoPreparo,
        String tipo,
        Boolean disponivel,
        Boolean destaque,
        Integer ordemExibicao
) {

    public ProdutoResponse(Produto produto) {
        this(
                produto.getId(),
                produto.getRestaurante().getId(),
                produto.getCategoria().getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getPrecoPromocional(),
                produto.getTempoPreparo(),
                String.valueOf(produto.getTipo()),
                produto.getDisponivel(),
                produto.getDestaque(),
                produto.getOrdemExibicao()
        );
    }
}
