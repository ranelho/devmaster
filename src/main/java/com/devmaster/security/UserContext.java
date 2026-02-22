package com.devmaster.security;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

/**
 * Contexto do usuário autenticado.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Getter
@Builder
public class UserContext {
    private UUID userId;
    private String username;
    private Set<String> roles;
    private Long restauranteId;
    
    public boolean isSuperAdmin() {
        return roles.contains("ROLE_SUPER_ADMIN");
    }
    
    public boolean isAdmin() {
        return roles.contains("ROLE_ADMIN");
    }
    
    public boolean isGerente() {
        return roles.contains("ROLE_GERENTE");
    }
    
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
