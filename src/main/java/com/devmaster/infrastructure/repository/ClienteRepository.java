package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de banco de dados relacionadas a Cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca cliente por telefone.
     * 
     * @param telefone Telefone do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByTelefone(String telefone);
    
    /**
     * Busca cliente por email.
     * 
     * @param email Email do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Busca cliente por CPF.
     * 
     * @param cpf CPF do cliente
     * @return Optional contendo o cliente se encontrado
     */
    Optional<Cliente> findByCpf(String cpf);
    
    /**
     * Verifica se existe cliente com o telefone informado.
     * 
     * @param telefone Telefone a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByTelefone(String telefone);
    
    /**
     * Verifica se existe cliente com o email informado.
     * 
     * @param email Email a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica se existe cliente com o CPF informado.
     * 
     * @param cpf CPF a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Lista clientes ativos com paginação.
     * 
     * @param ativo Status ativo
     * @param pageable Configuração de paginação
     * @return Página de clientes
     */
    Page<Cliente> findByAtivo(Boolean ativo, Pageable pageable);
    
    /**
     * Busca clientes por nome (busca parcial).
     * 
     * @param nomeCompleto Nome a buscar
     * @param pageable Configuração de paginação
     * @return Página de clientes
     */
    Page<Cliente> findByNomeCompletoContainingIgnoreCase(String nomeCompleto, Pageable pageable);
    
    /**
     * Conta total de clientes ativos.
     * 
     * @param ativo Status ativo
     * @return Total de clientes ativos
     */
    long countByAtivo(Boolean ativo);
}
