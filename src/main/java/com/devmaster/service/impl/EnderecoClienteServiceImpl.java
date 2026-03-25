package com.devmaster.service.impl;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.domain.EnderecoCliente;
import com.devmaster.handler.APIException;
import com.devmaster.infra.EnderecoClienteRepository;
import com.devmaster.service.EnderecoClienteService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EnderecoClienteServiceImpl implements EnderecoClienteService {

    private final EnderecoClienteRepository enderecoClienteRepository;

    @Override
    public List<EnderecoCliente> findAll() {
        return this.enderecoClienteRepository.findAll();
    }

    @Override
    public EnderecoCliente findById(Long id) {
        return this.enderecoClienteRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço de Cliente com id: " + id + " não encontrado"));
    }

    @Override
    public List<EnderecoCliente> findAllByClienteId(Long clienteId) {
        if (!enderecoClienteRepository.clienteExiste(clienteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Cliente com id: " + clienteId + " não encontrado");
        }
        return this.enderecoClienteRepository.findAllByClienteId(clienteId);
    }

    @Override
    public EnderecoCliente criar(EnderecoClienteRequest request) {
        if (!enderecoClienteRepository.clienteExiste(request.clienteId())) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Cliente com id: " + request.clienteId() + " não encontrado");
        }
        final var response = this.enderecoClienteRepository.save(new EnderecoCliente(request));

        return request.padrao() ? this.alterarPadrao(response.getId()) : response;
    }

    @Override
    public EnderecoCliente atualizar(Long id, EnderecoClienteRequest request) {
        final var enderecoCliente = this.findById(id);
        enderecoCliente.update(request);
        if(request.padrao()) {
            this.alterarPadrao(id);
        }
        return this.enderecoClienteRepository.save(enderecoCliente);
    }

    @Override
    @Transactional
    public EnderecoCliente alterarPadrao(Long id) {
        final var enderecoCliente = this.findById(id);
        if(enderecoCliente.getPadrao()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Endereço já é o padrão");
        }
        final var enderecos = this.findAllByClienteId(enderecoCliente.getClienteId())
                .stream()
                .map(endereco -> {
                    endereco.setPadrao(false);
                    return endereco;
                })
                .toList();
        this.enderecoClienteRepository.saveAll(enderecos);
        enderecoCliente.setPadrao(true);
        return this.enderecoClienteRepository.save(enderecoCliente);
    }

    @Override
    public void deletar(Long id) {
        if(!this.enderecoClienteRepository.existsById(id)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Endereço de Cliente com id: " + id + " não encontrado");
        }
        this.enderecoClienteRepository.deleteById(id);
    }
}
