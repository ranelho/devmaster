package com.devmaster.application.service;

import com.devmaster.application.api.request.CriarUsuarioRestauranteRequest;
import com.devmaster.application.api.request.VincularUsuarioRestauranteRequest;
import com.devmaster.application.api.response.UsuarioRestauranteResponse;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para gerenciar vínculos entre usuários e restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface UsuarioRestauranteService {
    
    /**
     * Cria um usuário no Auth Service e vincula ao restaurante.
     * Fluxo completo: criar usuário + vincular ao restaurante.
     */
    UsuarioRestauranteResponse criarEVincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        CriarUsuarioRestauranteRequest request
    );
    
    /**
     * Vincula um usuário existente a um restaurante com uma role específica.
     */
    UsuarioRestauranteResponse vincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        VincularUsuarioRestauranteRequest request
    );
    
    /**
     * Lista todos os usuários vinculados a um restaurante.
     */
    List<UsuarioRestauranteResponse> listarUsuariosDoRestaurante(
        UUID usuarioAutenticado,
        Long restauranteId
    );
    
    /**
     * Lista usuários de um restaurante por role.
     */
    List<UsuarioRestauranteResponse> listarUsuariosPorRole(
        UUID usuarioAutenticado,
        Long restauranteId,
        RoleRestaurante role
    );
    
    /**
     * Busca o vínculo de um usuário com um restaurante.
     */
    UsuarioRestauranteResponse buscarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    );
    
    /**
     * Desativa o vínculo de um usuário com um restaurante.
     */
    void desativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    );
    
    /**
     * Ativa o vínculo de um usuário com um restaurante.
     */
    void ativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    );
    
    /**
     * Verifica se um usuário tem acesso a um restaurante.
     */
    boolean temAcessoAoRestaurante(UUID usuarioId, Long restauranteId);
    
    /**
     * Busca a role de um usuário em um restaurante.
     */
    RoleRestaurante buscarRole(UUID usuarioId, Long restauranteId);
    
    /**
     * Busca o ID do restaurante vinculado ao usuário.
     */
    Long buscarRestauranteIdDoUsuario(UUID usuarioId);
}
