package com.devmaster.cliente.infra;

import com.devmaster.cliente.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteSpringDataJPARepository extends JpaRepository<Cliente, UUID>{
    Optional<Cliente> findByCpf(String cpf);

    boolean existsByCpf(String cpfFormatado);
}