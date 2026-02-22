package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.application.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
    public TipoPagamentoResponse criarTipoPagamento(TipoPagamentoRequest request) {
        return tipoPagamentoService.criarTipoPagamento(null, request);
    }
    
    @Override
    public TipoPagamentoResponse buscarTipoPagamento(Long id) {
        return tipoPagamentoService.buscarTipoPagamento(null, id);
    }
    
    @Override
    public List<TipoPagamentoResponse> listarTiposPagamento(Boolean ativo) {
        return tipoPagamentoService.listarTiposPagamento(null, ativo);
    }
    
    @Override
    public TipoPagamentoResponse atualizarTipoPagamento(Long id, TipoPagamentoRequest request) {
        return tipoPagamentoService.atualizarTipoPagamento(null, id, request);
    }
    
    @Override
    public void ativarTipoPagamento(Long id) {
        tipoPagamentoService.ativarTipoPagamento(null, id);
    }
    
    @Override
    public void desativarTipoPagamento(Long id) {
        tipoPagamentoService.desativarTipoPagamento(null, id);
    }
    
    @Override
    public void removerTipoPagamento(Long id) {
        tipoPagamentoService.removerTipoPagamento(null, id);
    }
}
