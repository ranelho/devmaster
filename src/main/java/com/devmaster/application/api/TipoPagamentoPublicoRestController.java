package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteTipoPagamentoResponse;
import com.devmaster.application.repository.RestauranteTipoPagamentoRepository;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TipoPagamentoPublicoRestController implements TipoPagamentoPublicoAPI {
    
    private final RestauranteTipoPagamentoRepository restauranteTipoPagamentoRepository;
    
    @Override
    public List<RestauranteTipoPagamentoResponse> listarTiposPagamentoRestaurante(Long restauranteId) {
        log.info("💳 [TIPOS-PAGAMENTO] Listando tipos de pagamento - RestauranteId: {}", restauranteId);
        
        List<RestauranteTipoPagamentoResponse> tiposPagamento = restauranteTipoPagamentoRepository
            .findByRestauranteIdAndAtivoTrue(restauranteId)
            .stream()
            .map(RestauranteTipoPagamentoResponse::from)
            .toList();
        
        if (tiposPagamento.isEmpty()) {
            log.warn("⚠️ [TIPOS-PAGAMENTO] Nenhum tipo de pagamento encontrado - RestauranteId: {}", restauranteId);
        } else {
            log.info("✅ [TIPOS-PAGAMENTO] {} tipos de pagamento encontrados", tiposPagamento.size());
        }
        
        return tiposPagamento;
    }
}
