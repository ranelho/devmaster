package com.devmaster.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Usuários Restaurante", description = "Gerenciamento de vínculos usuário-restaurante")
@RequestMapping("/v1/usuarios-restaurante")
public interface UsuarioRestauranteAPI {
    
    @Operation(summary = "Buscar restaurante do usuário autenticado", 
               description = "Retorna o ID do restaurante ao qual o usuário está vinculado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não vinculado a nenhum restaurante")
    })
    @GetMapping("/meu-restaurante")
    ResponseEntity<Map<String, Object>> buscarMeuRestaurante(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioId
    );
}
