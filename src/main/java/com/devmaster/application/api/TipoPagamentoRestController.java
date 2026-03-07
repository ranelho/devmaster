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
        var response = tipoPagamentoService.findAll();
        return ResponseEntity.ok(TipoPagamentoResponse.convert(response));
    }

    @Override
    public ResponseEntity<Page<TipoPagamentoResponse>> buscarTodosPaginado(Pageable pageable) {
        var response = tipoPagamentoService.findAllPageable(pageable);
        return ResponseEntity.ok(TipoPagamentoResponse.convertPageble(response));
    }

    @Override
    public ResponseEntity<TipoPagamentoResponse> atualizar(Long id, TipoPagamentoUpdateRequest request) {
        TipoPagamentoResponse response =
                new TipoPagamentoResponse(tipoPagamentoService.update(id, request));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TipoPagamentoResponse> ativar(Long id) {
        return ResponseEntity.ok( new TipoPagamentoResponse(tipoPagamentoService.ativar(id)));
    }

    @Override
    public ResponseEntity<TipoPagamentoResponse> inativar(Long id) {
        return ResponseEntity.ok( new TipoPagamentoResponse(tipoPagamentoService.inativar(id)));
    }
}
