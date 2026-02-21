package com.devmaster.application.service;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de serviço para operações relacionadas a Cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface ClienteService {
    
    /**
     * Cria um novo cliente.
     * Endpoint público - não requer autenticação.
     * 
     * @param request Dados do cliente
     * @return Cliente criado
     */
    ClienteResponse criarCliente(ClienteRequest request);
    
    /**
     * Busca cliente por ID.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @return Dados do cliente
     */
    ClienteResponse buscarCliente(Long clienteId);
    
    /**
     * Busca cliente por telefone.
     * Endpoint público - não requer autenticação.
     * 
     * @param telefone Telefone do cliente
     * @return Dados do cliente
     */
    ClienteResponse buscarClientePorTelefone(String telefone);
    
    /**
     * Lista todos os clientes com paginação.
     * Endpoint protegido - requer autenticação.
     * 
     * @param ativo Filtro por status ativo (opcional)
     * @param nome Filtro por nome (opcional)
     * @param pageable Configuração de paginação
     * @return Página de clientes
     */
    Page<ClienteResponse> listarClientes(Boolean ativo, String nome, Pageable pageable);
    
    /**
     * Atualiza dados de um cliente.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param request Dados a atualizar
     * @return Cliente atualizado
     */
    ClienteResponse atualizarCliente(Long clienteId, AtualizarClienteRequest request);
    
    /**
     * Desativa um cliente.
     * Endpoint protegido - requer autenticação ADMIN.
     * 
     * @param clienteId ID do cliente
     */
    void desativarCliente(Long clienteId);
    
    /**
     * Reativa um cliente.
     * Endpoint protegido - requer autenticação ADMIN.
     * 
     * @param clienteId ID do cliente
     */
    void reativarCliente(Long clienteId);
}
