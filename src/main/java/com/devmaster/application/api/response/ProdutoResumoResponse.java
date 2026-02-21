package com.devmaster.application.api.response;

import com.devmaster.domain.Produto;

import java.math.BigDecimal;

/**
 * Response resumido para Produto (listagens).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ProdutoResumoResponse(
    Long id,
    Long restauranteId,
    Long categoriaId,
    String categoriaNome,
    String nome,
    String descricao,
    BigDecimal preco,
    BigDecimal precoPromocional,
    BigDecimal precoFinal,
    Integer tempoPreparo,
    Boolean disponivel,
    Boolean destaque,
    String imagemPrincipalUrl
) {
    public static ProdutoResumoResponse from(Produto produto, String imagemPrincipalUrl) {
        return new ProdutoResumoResponse(
            produto.getId(),
            produto.getRestaurante().getId(),
            produto.getCategoria().getId(),
            produto.getCategoria().getNome(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getPrecoPromocional(),
            produto.getPrecoFinal(),
            produto.getTempoPreparo(),
            produto.getDisponivel(),
            produto.getDestaque(),
            imagemPrincipalUrl
        );
    }
}
