package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularTipoPagamentoRequest;
import com.devmaster.application.api.response.RestauranteTipoPagamentoResponse;
import com.devmaster.application.repository.RestauranteTipoPagamentoRepository;
import com.devmaster.domain.Restaurante;
import com.devmaster.domain.RestauranteTipoPagamento;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.TipoPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestauranteTipoPagamentoRestController implements RestauranteTipoPagamentoAPI {
    
    private final RestauranteTipoPagamentoRepository restauranteTipoPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    
    @Override
    public ResponseEntity<RestauranteTipoPagamentoResponse> vincularTipoPagamento(Long restauranteId, VincularTipoPagamentoRequest request) {

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
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vinculo.getId())
                .toUri();
        return ResponseEntity.created(location).body(RestauranteTipoPagamentoResponse.from(vinculo));
    }
    
    @Override
    public ResponseEntity<List<RestauranteTipoPagamentoResponse>> listarTiposPagamento(Long restauranteId) {
        List<RestauranteTipoPagamentoResponse> response = restauranteTipoPagamentoRepository.findByRestauranteId(restauranteId)
            .stream()
            .map(RestauranteTipoPagamentoResponse::from)
            .toList();
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> desvincularTipoPagamento(Long restauranteId, Long tipoPagamentoId) {
        RestauranteTipoPagamento vinculo = restauranteTipoPagamentoRepository
            .findByRestauranteIdAndTipoPagamentoId(restauranteId, tipoPagamentoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Vínculo não encontrado"));
        
        restauranteTipoPagamentoRepository.delete(vinculo);
        return ResponseEntity.noContent().build();
    }
}
