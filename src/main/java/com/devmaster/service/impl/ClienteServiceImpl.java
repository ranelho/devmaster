package com.devmaster.service.impl;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.domain.Cliente;
import com.devmaster.handler.APIException;
import com.devmaster.infra.ClienteRepository;
import com.devmaster.service.ClienteService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente com id: " + id + " não encontrado"));
    }

    @Override
    @Transactional
    public Cliente criar(ClienteRequest request) {

        request.email().ifPresent(email -> {
            if(clienteRepository.existsByEmail(email)) {
                throw APIException.build(HttpStatus.NOT_FOUND, "Email já cadastrado");
            }
        });
        request.cpf().ifPresent(cpf -> {
            if(clienteRepository.existsByCpf(cpf)) {
                throw APIException.build(HttpStatus.NOT_FOUND, "CPF já cadastrado");
            }
        });

        if(clienteRepository.existsByTelefone(request.telefone())) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Telefone já cadastrado");
        }

        return this.clienteRepository.save(new Cliente(request));
    }

    @Override
    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        final var cliente = this.findById(id);
        cliente.update(request);
        return this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente alternarAtivo(Long id) {
        final var cliente = this.findById(id);
        cliente.alternar();
        return clienteRepository.save(cliente);
    }
}
