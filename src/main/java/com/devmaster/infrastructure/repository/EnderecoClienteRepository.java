package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Cliente;
import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {

    List<EnderecoCliente> findByCliente(Cliente cliente);

    List<EnderecoCliente> findByClienteId(Long clienteId);

    Optional<EnderecoCliente> findByClienteIdAndPadrao(Long clienteId, Boolean padrao);

    Optional<EnderecoCliente> findByIdAndClienteId(Long id, Long clienteId);

    @Modifying
    @Query("UPDATE EnderecoCliente e SET e.padrao = false WHERE e.cliente.id = :clienteId")
    void removerPadraoTodosEnderecos(@Param("clienteId") Long clienteId);

    long countByClienteId(Long clienteId);

    boolean existsByClienteIdAndPadrao(Long clienteId, Boolean padrao);
}
