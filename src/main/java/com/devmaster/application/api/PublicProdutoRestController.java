package com.devmaster.application.api;

import com.devmaster.application.api.response.ProdutoResponse;
import com.devmaster.application.api.response.ProdutoResumoResponse;
import com.devmaster.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProdutoResumoResponse>> listarProdutos(Long restauranteId, Long categoriaId, Boolean disponivel, Boolean destaque) {
        List<ProdutoResumoResponse> response = produtoService.listarProdutosPorRestaurante(restauranteId, categoriaId, disponivel, destaque);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProdutoResponse> buscarProduto(Long restauranteId, Long produtoId) {
        ProdutoResponse response = produtoService.buscarProdutoCompleto(restauranteId, produtoId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ProdutoResumoResponse>> listarProdutosDestaque(Long restauranteId, int limite) {
        List<ProdutoResumoResponse> response = produtoService.listarProdutosDestaque(restauranteId, limite);
        return ResponseEntity.ok(response);
    }
}
