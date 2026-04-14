package com.devmaster.infra;

import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {

    List<EnderecoCliente> findAllByClienteId(Long clienteId);

}
