package com.devmaster.application.api;

import com.devmaster.application.api.request.DescontoRequest;
import com.devmaster.application.api.response.DescontoResponse;
import com.devmaster.application.service.DescontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DescontoController implements DescontoAPI {
    
    private final DescontoService descontoService;
    
    @Override
    public ResponseEntity<DescontoResponse> criar(DescontoRequest request) {
        DescontoResponse response = descontoService.criar(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<DescontoResponse> atualizar(Long id, DescontoRequest request) {
        DescontoResponse response = descontoService.atualizar(null, id, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<DescontoResponse> buscarPorId(Long id) {
        DescontoResponse response = descontoService.buscarPorId(null, id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<DescontoResponse>> listarTodos() {
        List<DescontoResponse> response = descontoService.listarTodos(null);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<DescontoResponse>> listarPorProduto(Long produtoId) {
        List<DescontoResponse> response = descontoService.listarPorProduto(null, produtoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<DescontoResponse>> listarVigentes() {
        List<DescontoResponse> response = descontoService.listarVigentes(null);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> deletar(Long id) {
        descontoService.deletar(null, id);
        return ResponseEntity.noContent().build();
    }
}
