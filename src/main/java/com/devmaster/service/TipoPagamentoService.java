package com.devmaster.service;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.domain.TipoPagamento;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TipoPagamentoService {
    TipoPagamento criar(TipoPagamentoRequest request);

    TipoPagamento findById(Long id);

    List<TipoPagamento> findAll();

    Page<TipoPagamento> findAllPageable(Pageable pageable);
}
