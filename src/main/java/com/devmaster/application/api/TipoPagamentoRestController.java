package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.application.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
    public TipoPagamentoResponse criarTipoPagamento(UUID usuarioId, TipoPagamentoRequest request) {
        return tipoPagamentoService.criarTipoPagamento(usuarioId, request);
    }
    
    @Override
    public TipoPagamentoResponse buscarTipoPagamento(UUID usuarioId, Long id) {
        return tipoPagamentoService.buscarTipoPagamento(usuarioId, id);
    }
    
    @Override
    public List<TipoPagamentoResponse> listarTiposPagamento(UUID usuarioId, Boolean ativo) {
        return tipoPagamentoService.listarTiposPagamento(usuarioId, ativo);
    }
    
    @Override
    public TipoPagamentoResponse atualizarTipoPagamento(UUID usuarioId, Long id, TipoPagamentoRequest request) {
        return tipoPagamentoService.atualizarTipoPagamento(usuarioId, id, request);
    }
    
    @Override
    public void ativarTipoPagamento(UUID usuarioId, Long id) {
        tipoPagamentoService.ativarTipoPagamento(usuarioId, id);
    }
    
    @Override
    public void desativarTipoPagamento(UUID usuarioId, Long id) {
        tipoPagamentoService.desativarTipoPagamento(usuarioId, id);
    }
    
    @Override
    public void removerTipoPagamento(UUID usuarioId, Long id) {
        tipoPagamentoService.removerTipoPagamento(usuarioId, id);
    }
}
