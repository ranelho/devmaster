package com.devmaster.application.service;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para TipoPagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface TipoPagamentoService {
    
    TipoPagamentoResponse criarTipoPagamento(UUID usuarioId, TipoPagamentoRequest request);
    TipoPagamentoResponse buscarTipoPagamento(UUID usuarioId, Long id);
    List<TipoPagamentoResponse> listarTiposPagamento(UUID usuarioId, Boolean ativo);
    TipoPagamentoResponse atualizarTipoPagamento(UUID usuarioId, Long id, TipoPagamentoRequest request);
    void ativarTipoPagamento(UUID usuarioId, Long id);
    void desativarTipoPagamento(UUID usuarioId, Long id);
    void removerTipoPagamento(UUID usuarioId, Long id);
}
