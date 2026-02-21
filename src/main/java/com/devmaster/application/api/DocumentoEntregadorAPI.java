package com.devmaster.application.api;

import com.devmaster.application.api.request.DocumentoEntregadorRequest;
import com.devmaster.application.api.request.VerificarDocumentoRequest;
import com.devmaster.application.api.response.DocumentoEntregadorResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.domain.enums.TipoDocumento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gestão de documentos de entregadores.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Documentos", description = "Gestão de documentos dos entregadores")
@RequestMapping("/entregadores/{entregadorId}/documentos")
public interface DocumentoEntregadorAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Adicionar documento", description = "Adiciona documento ao cadastro do entregador. Requer role SUPER_ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Documento adicionado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado"),
        @ApiResponse(responseCode = "409", description = "Documento do tipo já existe")
    })
    DocumentoEntregadorResponse adicionarDocumento(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Valid @RequestBody DocumentoEntregadorRequest request
    );
    
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar documentos", description = "Lista todos os documentos do entregador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de documentos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    List<DocumentoEntregadorResponse> listarDocumentos(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId
    );
    
    @GetMapping("/{documentoId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar documento", description = "Busca documento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    DocumentoEntregadorResponse buscarDocumento(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "ID do documento") @PathVariable Long documentoId
    );
    
    @GetMapping("/tipo/{tipoDocumento}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar documento por tipo", description = "Busca documento por tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    DocumentoEntregadorResponse buscarDocumentoPorTipo(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "Tipo do documento") @PathVariable TipoDocumento tipoDocumento
    );
    
    @PutMapping("/{documentoId}/verificar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Verificar documento", description = "Verifica ou remove verificação de documento. Requer role SUPER_ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento verificado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    DocumentoEntregadorResponse verificarDocumento(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "ID do documento") @PathVariable Long documentoId,
        @Valid @RequestBody VerificarDocumentoRequest request
    );
    
    @DeleteMapping("/{documentoId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remover documento", description = "Remove documento do cadastro. Requer role SUPER_ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Documento removido com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    MessageResponse removerDocumento(
        Authentication authentication,
        @Parameter(description = "ID do entregador") @PathVariable Long entregadorId,
        @Parameter(description = "ID do documento") @PathVariable Long documentoId
    );
}
