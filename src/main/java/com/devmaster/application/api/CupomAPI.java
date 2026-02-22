package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCupomRequest;
import com.devmaster.application.api.request.CupomRequest;
import com.devmaster.application.api.request.ValidarCupomRequest;
import com.devmaster.application.api.response.CupomResponse;
import com.devmaster.application.api.response.ValidacaoCupomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API REST para gerenciamento de Cupons.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Cupons", description = "Endpoints para gerenciamento de cupons de desconto")
@RequestMapping("/v1/cupons")
public interface CupomAPI {
    
    @Operation(summary = "Criar novo cupom", description = "Cria um novo cupom de desconto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Código já cadastrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CupomResponse criarCupom(
        @Parameter(description = "Dados do cupom", required = true)
        @Valid @RequestBody CupomRequest request
    );
    
    @Operation(summary = "Buscar cupom por ID", description = "Retorna os dados de um cupom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @GetMapping("/{cupomId}")
    CupomResponse buscarCupom(
        @Parameter(description = "ID do cupom", required = true)
        @PathVariable Long cupomId
    );
    
    @Operation(summary = "Buscar cupom por código", description = "Retorna os dados de um cupom pelo código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    CupomResponse buscarCupomPorCodigo(
        @Parameter(description = "Código do cupom", required = true)
        @PathVariable String codigo
    );
    
    @Operation(summary = "Listar cupons", description = "Lista cupons com filtros e paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cupons retornada com sucesso")
    })
    @GetMapping
    Page<CupomResponse> listarCupons(
        @Parameter(description = "Filtrar por status ativo")
        @RequestParam(required = false) Boolean ativo,
        
        @Parameter(description = "Filtrar apenas cupons válidos")
        @RequestParam(required = false) Boolean validos,
        
        @PageableDefault(size = 20) Pageable pageable
    );
    
    @Operation(summary = "Listar cupons válidos", description = "Lista todos os cupons válidos no momento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cupons retornada com sucesso")
    })
    @GetMapping("/validos")
    List<CupomResponse> listarCuponsValidos();
    
    @Operation(summary = "Atualizar cupom", description = "Atualiza os dados de um cupom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupom atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @PutMapping("/{cupomId}")
    CupomResponse atualizarCupom(
        @Parameter(description = "ID do cupom", required = true)
        @PathVariable Long cupomId,
        
        @Parameter(description = "Dados para atualização", required = true)
        @Valid @RequestBody AtualizarCupomRequest request
    );
    
    @Operation(summary = "Ativar cupom", description = "Ativa um cupom desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @PatchMapping("/{cupomId}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void ativarCupom(
        @Parameter(description = "ID do cupom", required = true)
        @PathVariable Long cupomId
    );
    
    @Operation(summary = "Desativar cupom", description = "Desativa um cupom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    @PatchMapping("/{cupomId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativarCupom(
        @Parameter(description = "ID do cupom", required = true)
        @PathVariable Long cupomId
    );
    
    @Operation(summary = "Remover cupom", description = "Remove um cupom que não foi utilizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
        @ApiResponse(responseCode = "400", description = "Cupom já foi utilizado")
    })
    @DeleteMapping("/{cupomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removerCupom(
        @Parameter(description = "ID do cupom", required = true)
        @PathVariable Long cupomId
    );
    
    @Operation(summary = "Validar cupom", description = "Valida um cupom e calcula o desconto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validação realizada com sucesso")
    })
    @PostMapping("/validar")
    ValidacaoCupomResponse validarCupom(
        @Parameter(description = "Dados para validação", required = true)
        @Valid @RequestBody ValidarCupomRequest request
    );
}
