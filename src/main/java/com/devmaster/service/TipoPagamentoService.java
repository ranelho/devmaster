package com.devmaster.service;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.domain.TipoPagamento;

public interface TipoPagamentoService {
    TipoPagamento criar(TipoPagamentoRequest request);

    TipoPagamento findById(Long id);
}
