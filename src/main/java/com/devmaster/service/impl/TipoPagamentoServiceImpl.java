package com.devmaster.service.impl;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.handler.APIException;
import com.devmaster.infra.TipoPagamentoRepository;
import com.devmaster.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TipoPagamentoServiceImpl implements TipoPagamentoService {

    private final TipoPagamentoRepository repository;

    @Override
    public TipoPagamento criar(TipoPagamentoRequest request) {

        if (repository.existsByCodigo(request.codigo())) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe um tipo de pagamento com este código");
        }

        TipoPagamento tipoPagamento = new TipoPagamento(request);

        return repository.save(tipoPagamento);
    }

    @Override
    public TipoPagamento findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tipo de pagamento não encontrado"));
    }
}
