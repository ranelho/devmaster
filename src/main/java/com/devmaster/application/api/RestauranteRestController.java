package com.devmaster.application.api;

import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestauranteRestController implements RestauranteApi {

    private final RestauranteService restauranteService;

    @Override
    public ResponseEntity<List<RestauranteResponse>> findAll() {
        final var restaurantes = restauranteService.findAll();
        return  ResponseEntity.ok(RestauranteResponse.convert(restaurantes));
    }

    @Override
    public ResponseEntity<RestauranteResponse> findById(Long id) {
        final var response = new RestauranteResponse(this.restauranteService.findById(id));
        return  ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestauranteResponse> criar(RestauranteRequest request) {
        final var response = new RestauranteResponse(this.restauranteService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<RestauranteResponse> atualizar(Long id, RestauranteRequest request) {
        final var response = new RestauranteResponse(this.restauranteService.atualizar(id,request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestauranteResponse> alternarAtivo(Long id) {
        final var response = new RestauranteResponse(this.restauranteService.alternarAtivo(id));
        return ResponseEntity.ok(response);
    }

}
