package com.devmaster.application.api.response;

import com.devmaster.domain.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    Integer ordemExibicao,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    List<ImagemProdutoResponse> imagens,
    List<GrupoOpcaoResponse> gruposOpcoes,
    // Campos de desconto
    BigDecimal percentualDesconto,
    BigDecimal valorOriginal,
    BigDecimal valorComDesconto,
    Boolean temDescontoVigente
) {
    public static ProdutoResumoResponse from(
        Produto produto,
        List<ImagemProdutoResponse> imagens,
        List<GrupoOpcaoResponse> gruposOpcoes,
        BigDecimal percentualDesconto,
        BigDecimal valorOriginal,
        BigDecimal valorComDesconto,
        Boolean temDescontoVigente
    ) {
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
            produto.getOrdemExibicao(),
            produto.getCriadoEm(),
            produto.getAtualizadoEm(),
            imagens,
            gruposOpcoes,
            percentualDesconto,
            valorOriginal,
            valorComDesconto,
            temDescontoVigente
        );
    }
}
