package com.devmaster.application.service;

import java.util.UUID;

/**
 * Interface para integração com o serviço de autenticação.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface AuthIntegrationService {
    
    /**
     * Cria um novo usuário no serviço de autenticação.
     * 
     * @param nome Nome do usuário
     * @param email Email do usuário
     * @param password Senha do usuário
     * @param isAdmin Se o usuário deve ter role ADMIN no auth service
     * @return UUID do usuário criado
     */
    UUID criarUsuario(String nome, String email, String password, boolean isAdmin);
    
    /**
     * Verifica se um usuário existe no serviço de autenticação.
     * 
     * @param usuarioId UUID do usuário
     * @return true se o usuário existe
     */
    boolean usuarioExiste(UUID usuarioId);
    
    /**
     * Busca informações de um usuário no serviço de autenticação.
     * 
     * @param usuarioId UUID do usuário
     * @return Informações do usuário
     */
    UsuarioAuthInfo buscarUsuario(UUID usuarioId);
    
    /**
     * DTO com informações do usuário do serviço de autenticação.
     */
    record UsuarioAuthInfo(
        UUID id,
        String nome,
        String email,
        boolean ativo
    ) {}
}
