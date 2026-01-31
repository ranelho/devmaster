package com.devmaster.cliente.infra;

import com.devmaster.cliente.application.repository.ClienteRepository;
import com.devmaster.cliente.domain.Cliente;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClienteInfraRepository implements ClienteRepository {
    private final ClienteSpringDataJPARepository jpaRepository;

    @Override
    public Cliente saveCliente(Cliente cliente) {
        try{
            return jpaRepository.save(cliente);
        }catch (DataIntegrityViolationException e) {
            throw APIException.build(HttpStatus.BAD_REQUEST,"Cliente já cadastrado, CPF: " + cliente.getCpf());
        }
    }

    @Override
    public Cliente findById(UUID idCliente) {
        return jpaRepository.findById(idCliente)
                .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST,"Cliente não encontrado!"));
    }

    @Override
    public Page<Cliente> getAllClientes(Pageable pageable) {
        Sort fixedSort = Sort.by(Sort.Direction.ASC, "nomeCompleto");
        Pageable pageableWithFixedSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), fixedSort);
        return jpaRepository.findAll(pageableWithFixedSort);
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf);
    }

    @Override
    public void existsByCpf(String cpfFormatado) {
        if (jpaRepository.existsByCpf(cpfFormatado)) {
            throw APIException.build(HttpStatus.BAD_REQUEST,"Cliente já cadastrado, CPF: " + cpfFormatado);
        }
    }
}