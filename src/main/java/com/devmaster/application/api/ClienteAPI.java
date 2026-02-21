package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * API REST para gestão de clientes.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Clientes", description = "Gestão de clientes do sistema")
@RequestMapping("/clientes")
public interface ClienteAPI {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar novo cliente ou retornar existente",
        description = """
            Cria um novo cliente no sistema ou retorna o cadastro existente se já houver cliente com o mesmo telefone ou CPF.
            
            Comportamento:
            - Se cliente já existe (por telefone ou CPF): retorna o cadastro existente
            - Se cliente estava inativo: reativa automaticamente
            - Se novo endereço for fornecido: adiciona ao cadastro (novo ou existente)
            - Se email já existe para OUTRO cliente: retorna erro 409
            
            Endpoint público - não requer autenticação.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso ou cadastro existente retornado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado para outro cliente")
    })
    ClienteResponse criarCliente(@Valid @RequestBody ClienteRequest request);

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna dados completos de um cliente. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ClienteResponse buscarCliente(@Parameter(description = "ID do cliente") @PathVariable Long id);

    @GetMapping("/telefone/{telefone}")
    @Operation(
            summary = "Buscar cliente por telefone",
            description = "Retorna dados do cliente pelo telefone. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ClienteResponse buscarClientePorTelefone(
            @Parameter(description = "Telefone do cliente") @PathVariable String telefone
    );

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Listar clientes",
            description = "Lista todos os clientes com filtros e paginação. Requer autenticação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    Page<ClienteResponse> listarClientes(
            Authentication authentication,
            @Parameter(description = "Filtrar por status ativo") @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Filtrar por nome") @RequestParam(required = false) String nome,
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "20") int size
    );

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza dados de um cliente. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "Telefone, email ou CPF já cadastrado")
    })
    ClienteResponse atualizarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Valid @RequestBody AtualizarClienteRequest request
    );

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Desativar cliente",
            description = "Desativa um cliente no sistema. Requer role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    MessageResponse desativarCliente(
            Authentication authentication,
            @Parameter(description = "ID do cliente") @PathVariable Long id
    );

    @PatchMapping("/{id}/reativar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Reativar cliente",
            description = "Reativa um cliente desativado. Requer role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente reativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    MessageResponse reativarCliente(
            Authentication authentication,
            @Parameter(description = "ID do cliente") @PathVariable Long id
    );
}
