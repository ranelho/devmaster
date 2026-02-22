package com.devmaster.application.api;

import com.devmaster.application.api.response.ProdutoResponse;
import com.devmaster.application.api.response.ProdutoResumoResponse;
import com.devmaster.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Produtos.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class PublicProdutoRestController implements PublicProdutoAPI {

    private final ProdutoService produtoService;

    @Override
    public List<ProdutoResumoResponse> listarProdutos(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque) {
        return produtoService.listarProdutosPorRestaurante(restauranteId, categoriaId, disponivel, destaque);
    }

    @Override
    public ProdutoResponse buscarProduto(Long restauranteId, Long produtoId) {
        return produtoService.buscarProdutoCompleto(restauranteId, produtoId);
    }

    @Override
    public List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite) {
        return produtoService.listarProdutosDestaque(restauranteId, limite);
    }
}
