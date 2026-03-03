package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTelefone(String telefone);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByTelefone(String telefone);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Page<Cliente> findByAtivo(Boolean ativo, Pageable pageable);

    Page<Cliente> findByNomeCompletoContainingIgnoreCase(String nomeCompleto, Pageable pageable);

}
