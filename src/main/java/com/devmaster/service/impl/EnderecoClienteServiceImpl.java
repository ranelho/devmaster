package com.devmaster.service.impl;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.domain.EnderecoCliente;
import com.devmaster.handler.APIException;
import com.devmaster.infra.EnderecoClienteRepository;
import com.devmaster.service.EnderecoClienteService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<EnderecoCliente> findAllPageable(Pageable pageable) {
        return this.enderecoClienteRepository.findAll(pageable);
    }

    @Override
    public EnderecoCliente findById(Long id) {
        return this.enderecoClienteRepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço de Cliente com id: " + id + " não encontrado"));
    }

    @Override
    public EnderecoCliente create(EnderecoClienteRequest enderecoClienteRequest) {
        return this.enderecoClienteRepository.save(new EnderecoCliente(enderecoClienteRequest));
    }

    @Override
    public EnderecoCliente update(Long id, EnderecoClienteRequest enderecoClienteRequest) {
        final var enderecoCliente = this.findById(id);
        enderecoCliente.update(enderecoClienteRequest);
        return this.enderecoClienteRepository.save(enderecoCliente);
    }

    @Override
    public void delete(Long id) {
        if(!this.enderecoClienteRepository.existsById(id)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Endereço de Cliente com id: " + id + " não encontrado");
        }
        this.enderecoClienteRepository.deleteById(id);
    }
}
