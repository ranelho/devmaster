package com.devmaster.application.api;

import com.devmaster.application.service.UsuarioRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller REST para gerenciar vínculos usuário-restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class UsuarioRestauranteRestController implements UsuarioRestauranteAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    
    @Override
    public ResponseEntity<java.util.Map<String, Object>> buscarMeuRestaurante(UUID usuarioId) {
        Long restauranteId = usuarioRestauranteService.buscarRestauranteIdDoUsuario(usuarioId);
        
        if (restauranteId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(java.util.Map.of("message", "Usuário não vinculado a nenhum restaurante"));
        }
        
        return ResponseEntity.ok(java.util.Map.of("restauranteId", restauranteId));
    }
}
