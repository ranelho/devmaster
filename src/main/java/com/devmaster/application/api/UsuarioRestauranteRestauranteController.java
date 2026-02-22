package com.devmaster.application.api;

import com.devmaster.application.api.request.CriarUsuarioRestauranteRequest;
import com.devmaster.application.api.request.VincularUsuarioRestauranteRequest;
import com.devmaster.application.api.response.UsuarioRestauranteResponse;
import com.devmaster.application.service.UsuarioRestauranteService;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gerenciar usuários de restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class UsuarioRestauranteRestauranteController implements UsuarioRestauranteRestauranteAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    
    @Override
    public ResponseEntity<UsuarioRestauranteResponse> criarEVincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        CriarUsuarioRestauranteRequest request
    ) {
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
        usuarioRestauranteService.desativarVinculo(usuarioAutenticado, restauranteId, usuarioId);
        
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> ativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        usuarioRestauranteService.ativarVinculo(usuarioAutenticado, restauranteId, usuarioId);
        
        return ResponseEntity.noContent().build();
    }
}
