package com.devmaster.cliente.application.repository;

import com.devmaster.cliente.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente saveCliente(Cliente cliente);
    Cliente findById(UUID idCliente);
    Page<Cliente> getAllClientes(Pageable pageable);
    Optional<Cliente> findByCpf(String cpf);
}