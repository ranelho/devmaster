package com.devmaster.infrastructure.repository;

import com.devmaster.domain.UsuarioRestaurante;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para gerenciar vínculos entre usuários e restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface UsuarioRestauranteRepository extends JpaRepository<UsuarioRestaurante, Long> {
    
    /**
     * Busca o vínculo ativo de um usuário com um restaurante.
     */
    Optional<UsuarioRestaurante> findByUsuarioIdAndRestauranteIdAndAtivoTrue(
        UUID usuarioId, 
        Long restauranteId
    );
    
    /**
     * Busca todos os vínculos ativos de um usuário.
     */
    List<UsuarioRestaurante> findByUsuarioIdAndAtivoTrue(UUID usuarioId);
    
    /**
     * Busca todos os usuários vinculados a um restaurante.
     */
    List<UsuarioRestaurante> findByRestauranteIdAndAtivoTrue(Long restauranteId);
    
    /**
     * Busca usuários de um restaurante por role.
     */
    List<UsuarioRestaurante> findByRestauranteIdAndRoleAndAtivoTrue(
        Long restauranteId, 
        RoleRestaurante role
    );
    
    /**
     * Verifica se um usuário tem acesso a um restaurante.
     */
    boolean existsByUsuarioIdAndRestauranteIdAndAtivoTrue(UUID usuarioId, Long restauranteId);
    
    /**
     * Verifica se já existe um vínculo (ativo ou inativo) entre usuário e restaurante.
     */
    boolean existsByUsuarioIdAndRestauranteId(UUID usuarioId, Long restauranteId);
    
    /**
     * Busca a role de um usuário em um restaurante específico.
     */
    @Query("SELECT ur.role FROM UsuarioRestaurante ur " +
           "WHERE ur.usuarioId = :usuarioId " +
           "AND ur.restaurante.id = :restauranteId " +
           "AND ur.ativo = true")
    Optional<RoleRestaurante> findRoleByUsuarioIdAndRestauranteId(
        @Param("usuarioId") UUID usuarioId,
        @Param("restauranteId") Long restauranteId
    );
    
    /**
     * Busca o ID do restaurante vinculado a um usuário.
     * Retorna o primeiro restaurante ativo encontrado.
     */
    @Query("SELECT ur.restaurante.id FROM UsuarioRestaurante ur " +
           "WHERE ur.usuarioId = :usuarioId " +
           "AND ur.ativo = true " +
           "ORDER BY ur.criadoEm ASC")
    Optional<Long> findRestauranteIdByUsuarioId(@Param("usuarioId") UUID usuarioId);
    
    /**
     * Conta quantos usuários com determinada role existem em um restaurante.
     */
    long countByRestauranteIdAndRoleAndAtivoTrue(Long restauranteId, RoleRestaurante role);
    
    /**
     * Busca todos os vínculos de um usuário (ativos e inativos).
     */
    List<UsuarioRestaurante> findByUsuarioId(UUID usuarioId);
}
