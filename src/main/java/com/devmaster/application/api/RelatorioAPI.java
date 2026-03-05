package com.devmaster.application.api;

import com.devmaster.application.api.response.VendasDashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Relatórios e Dashboard", description = "Módulo de relatórios e métricas")
@RequestMapping("/v1/restaurantes/{restauranteId}")
@SecurityRequirement(name = "bearerAuth")
public interface RelatorioAPI {

    @Operation(
        summary = "Relatório de Vendas (Excel)",
        description = "Gera um arquivo Excel com o relatório de vendas. Filtro opcional por data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Arquivo Excel gerado com sucesso",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        ),
        @ApiResponse(responseCode = "403", description = "Acesso negado (Requer ADMIN ou GERENTE)")
    })
    @GetMapping("/relatorios/vendas/excel")
    ResponseEntity<Resource> gerarRelatorioVendas(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId,
        @Parameter(description = "Data inicial (yyyy-MM-dd)") @RequestParam(required = false) LocalDate inicio,
        @Parameter(description = "Data final (yyyy-MM-dd)") @RequestParam(required = false) LocalDate fim
    );

    @Operation(
        summary = "Relatório de Produtos Mais Vendidos (Excel)",
        description = "Gera um arquivo Excel com os produtos mais vendidos. Filtro opcional por data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Arquivo Excel gerado com sucesso",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        ),
        @ApiResponse(responseCode = "403", description = "Acesso negado (Requer ADMIN ou GERENTE)")
    })
    @GetMapping("/relatorios/produtos-mais-vendidos/excel")
    ResponseEntity<Resource> gerarRelatorioProdutosMaisVendidos(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId,
        @Parameter(description = "Data inicial (yyyy-MM-dd)") @RequestParam(required = false) LocalDate inicio,
        @Parameter(description = "Data final (yyyy-MM-dd)") @RequestParam(required = false) LocalDate fim
    );

    @Operation(
        summary = "Dashboard de Vendas",
        description = "Retorna métricas consolidadas para o dashboard de vendas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Métricas recuperadas com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado (Requer ADMIN ou GERENTE)")
    })
    @GetMapping("/dashboard/vendas")
    ResponseEntity<VendasDashboardResponse> buscarDashboardVendas(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId,
        @Parameter(description = "Data inicial (yyyy-MM-dd)") @RequestParam(required = false) LocalDate inicio,
        @Parameter(description = "Data final (yyyy-MM-dd)") @RequestParam(required = false) LocalDate fim
    );
}
