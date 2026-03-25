package com.devmaster.service;


import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.domain.EnderecoCliente;

import java.util.List;

public interface EnderecoClienteService {

    List<EnderecoCliente> findAll();

    EnderecoCliente findById(Long id);

    List<EnderecoCliente> findAllByClienteId(Long clienteId);

    EnderecoCliente criar(EnderecoClienteRequest request);

    EnderecoCliente atualizar(Long id, EnderecoClienteRequest request);

    EnderecoCliente alterarPadrao(Long id);

    void deletar(Long id);
}
