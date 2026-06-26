package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.service.EnderecoRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnderecoRestauranteRestController implements EnderecoRestauranteApi {

    private final EnderecoRestauranteService enderecoRestauranteService;

    @Override
    public ResponseEntity<List<EnderecoRestauranteResponse>> findAll() {
        final var enderecoRestaurantes = enderecoRestauranteService.findAll();
        return ResponseEntity.ok(EnderecoRestauranteResponse.convert(enderecoRestaurantes));
    }

    @Override
    public ResponseEntity<EnderecoRestauranteResponse> findById(Long id) {
        final var response = new EnderecoRestauranteResponse(this.enderecoRestauranteService.findById(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<EnderecoRestauranteResponse>> findAllByRestauranteId(Long id) {
        final var response = EnderecoRestauranteResponse.convert(this.enderecoRestauranteService.findAllByRestauranteId(id));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EnderecoRestauranteResponse> criar(EnderecoRestauranteRequest request) {
        final var response = new EnderecoRestauranteResponse(this.enderecoRestauranteService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<EnderecoRestauranteResponse> atualizar(Long id, EnderecoRestauranteRequest request) {
        final var response = new EnderecoRestauranteResponse(this.enderecoRestauranteService.atualizar(id,request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        this.enderecoRestauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
