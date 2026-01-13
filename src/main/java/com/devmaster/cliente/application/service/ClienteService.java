package com.devmaster.cliente.application.service;

import com.devmaster.cliente.application.api.ClienteListResponse;
import com.devmaster.cliente.application.api.ClienteRequest;
import com.devmaster.cliente.application.api.ClienteResponse;
import com.devmaster.cliente.application.api.EditaClienteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClienteService {
    ClienteResponse saveCliente(ClienteRequest clienteRequest);
    ClienteResponse findById(UUID idCliente);
    Page<ClienteListResponse> getAllClientes(Pageable pageable);
    void updateCliente(UUID idCliente, EditaClienteRequest editaClienteRequest);
    ClienteResponse getByCpf(String cpf);
}
