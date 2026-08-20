package com.devmaster.application.api;

import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriaRestController implements CategoriaApi {

    private final CategoriaService categoriaService;


    @Override
    public ResponseEntity<List<CategoriaResponse>> findAllAtivoByRestaurante(Long id) {
        final var categorias = this.categoriaService.findAllAtivoTrueByRestauranteId(id);
        return ResponseEntity.ok(CategoriaResponse.convert(categorias));
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> findAllInativoByRestaurante(Long id) {
        final var categorias = this.categoriaService.findAllAtivoFalseByRestauranteId(id);
        return ResponseEntity.ok(CategoriaResponse.convert(categorias));
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CategoriaRequest request) {
        final var response = new CategoriaResponse(this.categoriaService.criar(request));
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<CategoriaResponse> atualizar(Long id, CategoriaRequest request) {
        final var response = new CategoriaResponse(this.categoriaService.atualizar(id, request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CategoriaResponse> alternarAtivo(Long id) {
        final var response = new CategoriaResponse(this.categoriaService.alternarAtivo(id));
        return ResponseEntity.ok(response);
    }
}
