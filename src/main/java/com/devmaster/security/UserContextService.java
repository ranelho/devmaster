package com.devmaster.restaurante.security;

import com.devmaster.restaurante.modules.store.service.UsuarioRestauranteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserContextService {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    
    public UserContext getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        
        UUID userId = UUID.fromString(auth.getName());
        Long restauranteId = null;
        
        try {
            restauranteId = usuarioRestauranteService.buscarRestauranteIdDoUsuario(userId);
        } catch (Exception e) {
            log.debug("Usuário {} não possui restaurante vinculado", userId);
        }
        
        return UserContext.builder()
            .userId(userId)
            .username(auth.getName())
            .roles(auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()))
            .restauranteId(restauranteId)
            .build();
    }
}

