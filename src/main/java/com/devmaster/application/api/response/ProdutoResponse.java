package com.devmaster.application.api.response;

import com.devmaster.domain.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProdutoResponse(
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
    List<GrupoOpcaoResponse> gruposOpcoes
) {
    public static ProdutoResponse from(
        Produto produto,
        List<ImagemProdutoResponse> imagens,
        List<GrupoOpcaoResponse> gruposOpcoes
    ) {
        return new ProdutoResponse(
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
            gruposOpcoes
        );
    }
}
