package com.devmaster.application.api;

import com.devmaster.application.api.response.ProdutoResponse;
import com.devmaster.application.api.response.ProdutoResumoResponse;
import com.devmaster.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST público para consulta de Produtos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicProdutoRestController implements PublicProdutoAPI {
    
    private final ProdutoService produtoService;
    
    @Override
    public List<ProdutoResumoResponse> listarProdutos(
            Long restauranteId,
            Long categoriaId,
            Boolean disponivel,
            Boolean destaque) {
        log.info("Listando produtos públicos do restaurante: {} (categoria: {}, disponivel: {}, destaque: {})",
                restauranteId, categoriaId, disponivel, destaque);
        return produtoService.listarProdutosPorRestaurante(restauranteId, categoriaId, disponivel, destaque);
    }
    
    @Override
    public ProdutoResponse buscarProduto(Long restauranteId, Long produtoId) {
        log.info("Buscando produto público: {} do restaurante: {}", produtoId, restauranteId);
        return produtoService.buscarProdutoCompleto(restauranteId, produtoId);
    }
    
    @Override
    public List<ProdutoResumoResponse> listarProdutosDestaque(Long restauranteId, int limite) {
        log.info("Listando produtos em destaque do restaurante: {} (limite: {})", restauranteId, limite);
        return produtoService.listarProdutosDestaque(restauranteId, limite);
    }
}
