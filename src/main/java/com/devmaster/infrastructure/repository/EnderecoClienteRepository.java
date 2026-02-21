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

/**
 * Repositório para operações de banco de dados relacionadas a EnderecoCliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface EnderecoClienteRepository extends JpaRepository<EnderecoCliente, Long> {
    
    /**
     * Lista todos os endereços de um cliente.
     * 
     * @param cliente Cliente
     * @return Lista de endereços
     */
    List<EnderecoCliente> findByCliente(Cliente cliente);
    
    /**
     * Lista todos os endereços de um cliente por ID.
     * 
     * @param clienteId ID do cliente
     * @return Lista de endereços
     */
    List<EnderecoCliente> findByClienteId(Long clienteId);
    
    /**
     * Busca endereço padrão do cliente.
     * 
     * @param clienteId ID do cliente
     * @return Optional contendo o endereço padrão se encontrado
     */
    Optional<EnderecoCliente> findByClienteIdAndPadrao(Long clienteId, Boolean padrao);
    
    /**
     * Busca endereço por ID e cliente.
     * 
     * @param id ID do endereço
     * @param clienteId ID do cliente
     * @return Optional contendo o endereço se encontrado
     */
    Optional<EnderecoCliente> findByIdAndClienteId(Long id, Long clienteId);
    
    /**
     * Remove marcação de padrão de todos os endereços do cliente.
     * 
     * @param clienteId ID do cliente
     */
    @Modifying
    @Query("UPDATE EnderecoCliente e SET e.padrao = false WHERE e.cliente.id = :clienteId")
    void removerPadraoTodosEnderecos(@Param("clienteId") Long clienteId);
    
    /**
     * Conta total de endereços de um cliente.
     * 
     * @param clienteId ID do cliente
     * @return Total de endereços
     */
    long countByClienteId(Long clienteId);
    
    /**
     * Verifica se cliente possui endereço padrão.
     * 
     * @param clienteId ID do cliente
     * @return true se possui, false caso contrário
     */
    boolean existsByClienteIdAndPadrao(Long clienteId, Boolean padrao);
}
