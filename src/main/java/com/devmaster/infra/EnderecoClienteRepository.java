package com.devmaster.infra;

import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {
}
