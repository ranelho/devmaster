package com.devmaster.service;


import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EnderecoClienteService {

    List<EnderecoCliente> findAll();

    Page<EnderecoCliente> findAllPageable(Pageable pageable);

    EnderecoCliente findById(Long id);

    EnderecoCliente criar(EnderecoClienteRequest enderecoClienteRequest);

    EnderecoCliente atualizar(Long id, EnderecoClienteRequest enderecoClienteRequest);

    void deletar(Long id);
}
