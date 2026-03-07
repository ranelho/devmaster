package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class TipoPagamentoRestController implements TipoPagamentoApi {

    private final TipoPagamentoService tipoPagamentoService;

    @Override
    public ResponseEntity<TipoPagamentoResponse> criar(TipoPagamentoRequest request) {

        TipoPagamentoResponse response = new TipoPagamentoResponse(tipoPagamentoService.criar(request));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<TipoPagamentoResponse> buscarPorId(Long id) {
        TipoPagamentoResponse response = new TipoPagamentoResponse(tipoPagamentoService.findById(id));
        return ResponseEntity.ok(response);
    }
}
