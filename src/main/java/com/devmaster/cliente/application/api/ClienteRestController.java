package com.devmaster.cliente.application.api;

import com.devmaster.cliente.application.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClienteRestController implements ClienteAPI {

    private final ClienteService clienteService;

    @Override
    public ClienteResponse saveCliente(ClienteRequest clienteRequest) {
        return clienteService.saveCliente(clienteRequest);
    }

    @Override
    public ClienteResponse getOneCliente(UUID idCliente) {
        return clienteService.findById(idCliente);
    }

    @Override
    public ClienteResponse getByCpf(String cpf) {
        return clienteService.getByCpf(cpf);
    }

    @Override
    public Page<ClienteListResponse> getAllClientes(Pageable pageable) {
        return clienteService.getAllClientes(pageable);
    }

    @Override
    public void updateCliente(UUID idCliente, @Valid  EditaClienteRequest editaClienteRequest) {
        clienteService.updateCliente(idCliente, editaClienteRequest);
    }

}