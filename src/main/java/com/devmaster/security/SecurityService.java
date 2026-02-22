package com.devmaster.security;

import com.devmaster.handler.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço centralizado para operações de segurança.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@Service
public class SecurityService {
    
    public UUID getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        return UUID.fromString(auth.getName());
    }
    
    public Set<String> getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Set.of();
        }
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    }
    
    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
    
    public boolean isSuperAdmin() {
        return hasRole("ROLE_SUPER_ADMIN");
    }
    
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    public boolean isGerente() {
        return hasRole("ROLE_GERENTE");
    }
}
