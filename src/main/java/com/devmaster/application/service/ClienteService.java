package com.devmaster.application.service;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClienteService {
    ClienteResponse criarCliente(ClienteRequest request);

    ClienteResponse buscarCliente(Long clienteId);

    ClienteResponse buscarClientePorTelefone(String telefone);

    Page<ClienteResponse> listarClientes(Boolean ativo, String nome, Pageable pageable);

    ClienteResponse atualizarCliente(Long clienteId, AtualizarClienteRequest request);

    void desativarCliente(Long clienteId);

    void reativarCliente(Long clienteId);
}
