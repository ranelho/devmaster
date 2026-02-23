package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularTipoPagamentoRequest;
import com.devmaster.application.api.response.RestauranteTipoPagamentoResponse;
import com.devmaster.application.repository.RestauranteTipoPagamentoRepository;
import com.devmaster.infrastructure.repository.TipoPagamentoRepository;
import com.devmaster.domain.Restaurante;
import com.devmaster.domain.RestauranteTipoPagamento;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestauranteTipoPagamentoRestController implements RestauranteTipoPagamentoAPI {
    
    private final RestauranteTipoPagamentoRepository restauranteTipoPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    
    @Override
    public RestauranteTipoPagamentoResponse vincularTipoPagamento(Long restauranteId, VincularTipoPagamentoRequest request) {
        log.info("🔗 [VINCULAR-TIPO-PAGAMENTO] RestauranteId: {}, TipoPagamentoId: {}", restauranteId, request.tipoPagamentoId());
        
        if (restauranteTipoPagamentoRepository.existsByRestauranteIdAndTipoPagamentoId(restauranteId, request.tipoPagamentoId())) {
            throw APIException.build(HttpStatus.CONFLICT, "Tipo de pagamento já vinculado ao restaurante");
        }
        
        TipoPagamento tipoPagamento = tipoPagamentoRepository.findById(request.tipoPagamentoId())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tipo de pagamento não encontrado"));
        
        RestauranteTipoPagamento vinculo = RestauranteTipoPagamento.builder()
            .restaurante(Restaurante.builder().id(restauranteId).build())
            .tipoPagamento(tipoPagamento)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .ativo(true)
            .build();
        
        vinculo = restauranteTipoPagamentoRepository.save(vinculo);
        
        log.info("✅ [VINCULAR-TIPO-PAGAMENTO] Tipo de pagamento vinculado com sucesso");
        
        return RestauranteTipoPagamentoResponse.from(vinculo);
    }
    
    @Override
    public List<RestauranteTipoPagamentoResponse> listarTiposPagamento(Long restauranteId) {
        log.info("📋 [LISTAR-TIPOS-PAGAMENTO] RestauranteId: {}", restauranteId);
        
        return restauranteTipoPagamentoRepository.findByRestauranteId(restauranteId)
            .stream()
            .map(RestauranteTipoPagamentoResponse::from)
            .toList();
    }
    
    @Override
    public void desvincularTipoPagamento(Long restauranteId, Long tipoPagamentoId) {
        log.info("🗑️ [DESVINCULAR-TIPO-PAGAMENTO] RestauranteId: {}, TipoPagamentoId: {}", restauranteId, tipoPagamentoId);
        
        RestauranteTipoPagamento vinculo = restauranteTipoPagamentoRepository
            .findByRestauranteIdAndTipoPagamentoId(restauranteId, tipoPagamentoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Vínculo não encontrado"));
        
        restauranteTipoPagamentoRepository.delete(vinculo);
        
        log.info("✅ [DESVINCULAR-TIPO-PAGAMENTO] Tipo de pagamento desvinculado com sucesso");
    }
}
