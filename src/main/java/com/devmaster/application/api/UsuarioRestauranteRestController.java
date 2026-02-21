package com.devmaster.application.api;

import com.devmaster.application.api.request.CriarUsuarioRestauranteRequest;
import com.devmaster.application.api.request.VincularUsuarioRestauranteRequest;
import com.devmaster.application.api.response.UsuarioRestauranteResponse;
import com.devmaster.application.service.UsuarioRestauranteService;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gerenciar vínculos usuário-restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UsuarioRestauranteRestController implements UsuarioRestauranteAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    
    @Override
    public ResponseEntity<java.util.Map<String, Object>> buscarMeuRestaurante(UUID usuarioId) {
        log.info("[BUSCAR-MEU-RESTAURANTE] Usuario: {}", usuarioId);
        
        Long restauranteId = usuarioRestauranteService.buscarRestauranteIdDoUsuario(usuarioId);
        
        if (restauranteId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(java.util.Map.of("message", "Usuário não vinculado a nenhum restaurante"));
        }
        
        return ResponseEntity.ok(java.util.Map.of("restauranteId", restauranteId));
    }
}

@Slf4j
@RestController
@RequiredArgsConstructor
class UsuarioRestauranteRestauranteController implements UsuarioRestauranteRestauranteAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    
    @Override
    public ResponseEntity<UsuarioRestauranteResponse> criarEVincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        CriarUsuarioRestauranteRequest request
    ) {
        log.info("[CRIAR-VINCULAR-USUARIO] Restaurante: {}, Email: {}, Role: {}", 
            restauranteId, request.email(), request.role());
        
        UsuarioRestauranteResponse response = usuarioRestauranteService.criarEVincularUsuario(
            usuarioAutenticado, restauranteId, request
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Override
    public ResponseEntity<UsuarioRestauranteResponse> vincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        VincularUsuarioRestauranteRequest request
    ) {
        log.info("[VINCULAR-USUARIO] Restaurante: {}, Usuario: {}, Role: {}", 
            restauranteId, request.usuarioId(), request.role());
        
        UsuarioRestauranteResponse response = usuarioRestauranteService.vincularUsuario(
            usuarioAutenticado, restauranteId, request
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Override
    public ResponseEntity<List<UsuarioRestauranteResponse>> listarUsuarios(
        UUID usuarioAutenticado,
        Long restauranteId,
        RoleRestaurante role
    ) {
        log.info("[LISTAR-USUARIOS] Restaurante: {}, Role: {}", restauranteId, role);
        
        List<UsuarioRestauranteResponse> usuarios;
        
        if (role != null) {
            usuarios = usuarioRestauranteService.listarUsuariosPorRole(
                usuarioAutenticado, restauranteId, role
            );
        } else {
            usuarios = usuarioRestauranteService.listarUsuariosDoRestaurante(
                usuarioAutenticado, restauranteId
            );
        }
        
        return ResponseEntity.ok(usuarios);
    }
    
    @Override
    public ResponseEntity<UsuarioRestauranteResponse> buscarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        log.info("[BUSCAR-VINCULO] Restaurante: {}, Usuario: {}", restauranteId, usuarioId);
        
        UsuarioRestauranteResponse response = usuarioRestauranteService.buscarVinculo(
            usuarioAutenticado, restauranteId, usuarioId
        );
        
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> desativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        log.info("[DESATIVAR-VINCULO] Restaurante: {}, Usuario: {}", restauranteId, usuarioId);
        
        usuarioRestauranteService.desativarVinculo(usuarioAutenticado, restauranteId, usuarioId);
        
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> ativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        log.info("[ATIVAR-VINCULO] Restaurante: {}, Usuario: {}", restauranteId, usuarioId);
        
        usuarioRestauranteService.ativarVinculo(usuarioAutenticado, restauranteId, usuarioId);
        
        return ResponseEntity.noContent().build();
    }
}
