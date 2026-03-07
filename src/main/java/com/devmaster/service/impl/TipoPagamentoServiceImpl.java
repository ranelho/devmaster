package com.devmaster.service.impl;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.request.TipoPagamentoUpdateRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.handler.APIException;
import com.devmaster.infra.TipoPagamentoRepository;
import com.devmaster.service.TipoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;


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

    @Override
    public List<TipoPagamento> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<TipoPagamento> findAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public TipoPagamento update(Long id, TipoPagamentoUpdateRequest request) {
        TipoPagamento tipoPagamento = findById(id);
        tipoPagamento.update(request);
        return repository.save(tipoPagamento);
    }

    //com lambda
/*    @Override
    public TipoPagamento update(Long id, TipoPagamentoUpdateRequest request) {
        return repository.findById(id)
                .map(tipoPagamento -> {
                    tipoPagamento.update(request);
                    return repository.save(tipoPagamento);
                })
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "TipoPagamento não encontrado"));
    }*/
}
