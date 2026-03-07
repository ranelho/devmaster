package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.request.TipoPagamentoUpdateRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;

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

    @Override
    public ResponseEntity<List<TipoPagamentoResponse>> buscarTodos() {
        List<TipoPagamentoResponse> response = tipoPagamentoService.findAll().stream()
                .map(TipoPagamentoResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<TipoPagamentoResponse>> buscarTodosPaginado(Pageable pageable) {
        Page<TipoPagamentoResponse> response = tipoPagamentoService.findAllPageable(pageable)
                .map(TipoPagamentoResponse::new);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TipoPagamentoResponse> atualizar(Long id, TipoPagamentoUpdateRequest request) {
        TipoPagamento tipoPagamento = tipoPagamentoService.update(id, request);
        TipoPagamentoResponse response = new TipoPagamentoResponse(tipoPagamento);
        return ResponseEntity.ok(response);
    }
}
