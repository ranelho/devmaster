package com.devmaster.application.api;

import com.devmaster.application.api.request.CriarUsuarioRestauranteRequest;
import com.devmaster.application.api.request.VincularUsuarioRestauranteRequest;
import com.devmaster.application.api.response.UsuarioRestauranteResponse;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API para gerenciar vínculos entre usuários e restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
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
    ResponseEntity<java.util.Map<String, Object>> buscarMeuRestaurante(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioId
    );
}

/**
 * API para gerenciar vínculos entre usuários e restaurantes (por restaurante).
 */
@Tag(name = "Usuários Restaurante", description = "Gerenciamento de vínculos usuário-restaurante")
@RequestMapping("/v1/restaurantes/{restauranteId}/usuarios")
interface UsuarioRestauranteRestauranteAPI {
    
    @Operation(summary = "Criar usuário e vincular ao restaurante", 
               description = "Cria um novo usuário no Auth Service e vincula ao restaurante com uma role específica")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado e vinculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/criar")
    ResponseEntity<UsuarioRestauranteResponse> criarEVincularUsuario(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Valid @RequestBody CriarUsuarioRestauranteRequest request
    );
    
    @Operation(summary = "Vincular usuário existente ao restaurante", 
               description = "Vincula um usuário já existente no Auth Service ao restaurante com uma role específica")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário vinculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Usuário já vinculado")
    })
    @PostMapping
    ResponseEntity<UsuarioRestauranteResponse> vincularUsuario(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Valid @RequestBody VincularUsuarioRestauranteRequest request
    );
    
    @Operation(summary = "Listar usuários do restaurante",
               description = "Lista todos os usuários vinculados ao restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping
    ResponseEntity<List<UsuarioRestauranteResponse>> listarUsuarios(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Filtrar por role")
        @RequestParam(required = false) RoleRestaurante role
    );
    
    @Operation(summary = "Buscar vínculo específico",
               description = "Busca o vínculo de um usuário específico com o restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vínculo encontrado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @GetMapping("/{usuarioId}")
    ResponseEntity<UsuarioRestauranteResponse> buscarVinculo(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID do usuário")
        @PathVariable UUID usuarioId
    );
    
    @Operation(summary = "Desativar vínculo",
               description = "Desativa o vínculo de um usuário com o restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vínculo desativado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @DeleteMapping("/{usuarioId}")
    ResponseEntity<Void> desativarVinculo(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID do usuário")
        @PathVariable UUID usuarioId
    );
    
    @Operation(summary = "Ativar vínculo",
               description = "Ativa um vínculo previamente desativado")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vínculo ativado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @PatchMapping("/{usuarioId}/ativar")
    ResponseEntity<Void> ativarVinculo(
        @Parameter(description = "ID do usuário autenticado", hidden = true)
        @RequestHeader("X-User-Id") UUID usuarioAutenticado,
        
        @Parameter(description = "ID do restaurante")
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID do usuário")
        @PathVariable UUID usuarioId
    );
}
