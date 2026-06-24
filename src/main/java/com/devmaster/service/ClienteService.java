package com.devmaster.service;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.domain.Cliente;

import java.util.List;

public interface ClienteService {

    List<Cliente> findAll();

    Cliente findById(Long id);

    Cliente criar(ClienteRequest request);

    Cliente atualizar(Long id, ClienteRequest request);

    Cliente alternarAtivo(Long id);

}
