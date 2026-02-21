package com.devmaster.application.service;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a EnderecoCliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface EnderecoClienteService {
    
    /**
     * Adiciona endereço ao cadastro do cliente.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param request Dados do endereço
     * @return Endereço criado
     */
    EnderecoClienteResponse adicionarEndereco(Long clienteId, EnderecoClienteRequest request);
    
    /**
     * Lista todos os endereços de um cliente.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @return Lista de endereços
     */
    List<EnderecoClienteResponse> listarEnderecos(Long clienteId);
    
    /**
     * Busca endereço específico.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param enderecoId ID do endereço
     * @return Dados do endereço
     */
    EnderecoClienteResponse buscarEndereco(Long clienteId, Long enderecoId);
    
    /**
     * Busca endereço padrão do cliente.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @return Endereço padrão
     */
    EnderecoClienteResponse buscarEnderecoPadrao(Long clienteId);
    
    /**
     * Atualiza dados de um endereço.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param enderecoId ID do endereço
     * @param request Dados a atualizar
     * @return Endereço atualizado
     */
    EnderecoClienteResponse atualizarEndereco(
        Long clienteId,
        Long enderecoId,
        EnderecoClienteRequest request
    );
    
    /**
     * Define endereço como padrão.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param enderecoId ID do endereço
     * @return Endereço atualizado
     */
    EnderecoClienteResponse definirEnderecoPadrao(Long clienteId, Long enderecoId);
    
    /**
     * Remove endereço do cadastro.
     * Endpoint público - não requer autenticação.
     * 
     * @param clienteId ID do cliente
     * @param enderecoId ID do endereço
     */
    void removerEndereco(Long clienteId, Long enderecoId);
}
