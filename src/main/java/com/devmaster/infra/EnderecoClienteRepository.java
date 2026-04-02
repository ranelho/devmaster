package com.devmaster.infra;

import com.devmaster.domain.EnderecoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {

    List<EnderecoCliente> findAllByClienteId(Long clienteId);

    @Query(value = "SELECT EXISTS( SELECT c.id FROM clientes c WHERE c.id=:cliente_id)", nativeQuery = true)
    Boolean clienteExiste(@Param("cliente_id") Long clienteId);
}
