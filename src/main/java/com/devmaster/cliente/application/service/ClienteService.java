package com.devmaster.cliente.application.service;

import com.devmaster.cliente.application.api.response.ClienteListResponse;
import com.devmaster.cliente.application.api.request.ClienteRequest;
import com.devmaster.cliente.application.api.response.ClienteResponse;
import com.devmaster.cliente.application.api.request.EditaClienteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClienteService {
    ClienteResponse saveCliente(ClienteRequest clienteRequest);
    ClienteResponse findById(UUID idCliente);
    Page<ClienteListResponse> getAllClientes(Pageable pageable);
    ClienteResponse updateCliente(UUID idCliente, EditaClienteRequest editaClienteRequest);
    ClienteResponse getByCpf(String cpf);
}
