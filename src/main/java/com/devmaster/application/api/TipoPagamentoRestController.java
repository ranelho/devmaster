package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.application.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Implementação da API de Tipos de Pagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class TipoPagamentoRestController implements TipoPagamentoAPI {
    
    private final TipoPagamentoService tipoPagamentoService;
    
    @Override
    public ResponseEntity<TipoPagamentoResponse> criarTipoPagamento(TipoPagamentoRequest request) {
        TipoPagamentoResponse response = tipoPagamentoService.criarTipoPagamento(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<TipoPagamentoResponse> buscarTipoPagamento(Long id) {
        TipoPagamentoResponse response = tipoPagamentoService.buscarTipoPagamento(null, id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<TipoPagamentoResponse>> listarTiposPagamento(Boolean ativo) {
        List<TipoPagamentoResponse> response = tipoPagamentoService.listarTiposPagamento(null, ativo);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<TipoPagamentoResponse> atualizarTipoPagamento(Long id, TipoPagamentoRequest request) {
        TipoPagamentoResponse response = tipoPagamentoService.atualizarTipoPagamento(null, id, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> ativarTipoPagamento(Long id) {
        tipoPagamentoService.ativarTipoPagamento(null, id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> desativarTipoPagamento(Long id) {
        tipoPagamentoService.desativarTipoPagamento(null, id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerTipoPagamento(Long id) {
        tipoPagamentoService.removerTipoPagamento(null, id);
        return ResponseEntity.noContent().build();
    }
}
