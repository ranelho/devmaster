package com.devmaster.cliente.application.service;

import com.devmaster.cliente.application.api.response.ClienteListResponse;
import com.devmaster.cliente.application.api.request.ClienteRequest;
import com.devmaster.cliente.application.api.response.ClienteResponse;
import com.devmaster.cliente.application.api.request.EditaClienteRequest;
import com.devmaster.cliente.application.repository.ClienteRepository;
import com.devmaster.cliente.domain.Cliente;
import com.devmaster.cliente.util.CpfUtil;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse saveCliente(ClienteRequest clienteRequest) {
        String cpfFormatado = CpfUtil.formatCpf(clienteRequest.cpf());
        clienteRepository.existsByCpf(cpfFormatado);
        Cliente cliente = clienteRepository.saveCliente(new Cliente(cpfFormatado, clienteRequest));
        return new ClienteResponse(cliente);
    }

    @Override
    public ClienteResponse findById(UUID idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente);
        return new ClienteResponse(cliente);
    }

    @Override
    public Page<ClienteListResponse> getAllClientes(Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.getAllClientes(pageable);
        return ClienteListResponse.convertePageable(clientes);
    }

    @Override
    public ClienteResponse updateCliente(UUID idCliente, EditaClienteRequest request) {
        Cliente cliente = clienteRepository.findById(idCliente);
        cliente.update(request);
        return new ClienteResponse(clienteRepository.saveCliente(cliente));
    }

    @Override
    public ClienteResponse getByCpf(String cpf) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST,"Cliente não encontrado!"));
        return new ClienteResponse(cliente);
    }
}