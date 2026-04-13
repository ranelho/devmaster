package com.devmaster.application.api;

import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.RestauranteApi;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public abstract class RestauranteRestController implements RestauranteApi {

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
    public ResponseEntity<Page<RestauranteResponse>> findAllPageable(Pageable pageable) {
        final var response = restauranteService.findAllPageable(pageable);
        return ResponseEntity.ok(RestauranteResponse.convertPageable(response));
    }

    @Override
    public ResponseEntity<RestauranteResponse> atualizar(Long id, RestauranteRequest request) {
        final var response = new RestauranteResponse(this.restauranteService.atualizar(id,request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestauranteResponse> ativar(Long id) {
        final var response = new RestauranteResponse(this.restauranteService.ativar(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RestauranteResponse> inativar(Long id) {
        final var response = new RestauranteResponse(this.restauranteService.inativar(id));
        return ResponseEntity.ok(response);
    }

}
