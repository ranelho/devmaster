package com.devmaster.application.api;

import com.devmaster.application.api.response.VendasDashboardResponse;
import com.devmaster.application.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class RelatorioRestController implements RelatorioAPI {

    private final RelatorioService relatorioService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<Resource> gerarRelatorioVendas(Long restauranteId, LocalDate inicio, LocalDate fim) {
        byte[] excelBytes = relatorioService.gerarRelatorioVendasExcel(restauranteId, inicio, fim);
        String filename = "relatorio_vendas_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(excelBytes));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<Resource> gerarRelatorioProdutosMaisVendidos(Long restauranteId, LocalDate inicio, LocalDate fim) {
        byte[] excelBytes = relatorioService.gerarRelatorioProdutosMaisVendidosExcel(restauranteId, inicio, fim);
        String filename = "produtos_mais_vendidos_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new ByteArrayResource(excelBytes));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<VendasDashboardResponse> buscarDashboardVendas(Long restauranteId, LocalDate inicio, LocalDate fim) {
        VendasDashboardResponse response = relatorioService.buscarDadosDashboard(restauranteId, inicio, fim);
        return ResponseEntity.ok(response);
    }
}
