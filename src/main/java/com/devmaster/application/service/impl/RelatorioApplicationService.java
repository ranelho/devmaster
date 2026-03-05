package com.devmaster.application.service.impl;

import com.devmaster.application.api.response.ProdutoMaisVendidoDTO;
import com.devmaster.application.api.response.VendaDiariaDTO;
import com.devmaster.application.api.response.VendasDashboardResponse;
import com.devmaster.application.service.RelatorioService;
import com.devmaster.domain.Pedido;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelatorioApplicationService implements RelatorioService {

    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public VendasDashboardResponse buscarDadosDashboard(Long restauranteId, LocalDate inicio, LocalDate fim) {
        LocalDateTime dataInicio = inicio != null ? inicio.atStartOfDay() : null;
        LocalDateTime dataFim = fim != null ? fim.atTime(LocalTime.MAX) : null;

        BigDecimal totalVendas = pedidoRepository.sumValorTotalByPeriodo(restauranteId, dataInicio, dataFim);
        Long quantidadePedidos = pedidoRepository.countPedidosEntreguesByPeriodo(restauranteId, dataInicio, dataFim);
        List<VendaDiariaDTO> vendasPorDia = pedidoRepository.findVendasDiarias(restauranteId, dataInicio, dataFim);

        if (totalVendas == null) totalVendas = BigDecimal.ZERO;
        if (quantidadePedidos == null) quantidadePedidos = 0L;

        BigDecimal ticketMedio = quantidadePedidos > 0 
                ? totalVendas.divide(BigDecimal.valueOf(quantidadePedidos), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        return VendasDashboardResponse.builder()
            .totalVendas(totalVendas)
            .quantidadePedidos(quantidadePedidos)
            .ticketMedio(ticketMedio)
            .vendasPorDia(vendasPorDia)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] gerarRelatorioVendasExcel(Long restauranteId, LocalDate inicio, LocalDate fim) {
        LocalDateTime dataInicio = inicio != null ? inicio.atStartOfDay() : null;
        LocalDateTime dataFim = fim != null ? fim.atTime(LocalTime.MAX) : null;

        List<Pedido> pedidos = pedidoRepository.findByPeriodo(restauranteId, dataInicio, dataFim);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório de Vendas");

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo para moeda
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("R$ #,##0.00"));

            // Cabeçalho
            String[] headers = {"ID", "Número Pedido", "Data", "Cliente", "Restaurante", "Status", "Pagamento", "Total"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Dados
            int rowIdx = 1;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Pedido pedido : pedidos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(pedido.getId());
                row.createCell(1).setCellValue(pedido.getNumeroPedido());
                row.createCell(2).setCellValue(pedido.getCriadoEm().format(dateFormatter));
                row.createCell(3).setCellValue(pedido.getClienteNome());
                row.createCell(4).setCellValue(pedido.getRestaurante().getNome());
                row.createCell(5).setCellValue(pedido.getStatus().getDescricao());
                row.createCell(6).setCellValue(pedido.getStatusPagamento().getDescricao());
                
                Cell totalCell = row.createCell(7);
                totalCell.setCellValue(pedido.getValorTotal().doubleValue());
                totalCell.setCellStyle(currencyStyle);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel de vendas", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório Excel");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] gerarRelatorioProdutosMaisVendidosExcel(Long restauranteId, LocalDate inicio, LocalDate fim) {
        LocalDateTime dataInicio = inicio != null ? inicio.atStartOfDay() : null;
        LocalDateTime dataFim = fim != null ? fim.atTime(LocalTime.MAX) : null;

        List<ProdutoMaisVendidoDTO> produtos = pedidoRepository.findProdutosMaisVendidos(restauranteId, dataInicio, dataFim);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Produtos Mais Vendidos");

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Estilo para moeda
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("R$ #,##0.00"));

            // Cabeçalho
            String[] headers = {"Produto", "Quantidade Vendida", "Valor Total"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Dados
            int rowIdx = 1;
            for (ProdutoMaisVendidoDTO produto : produtos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(produto.getProdutoNome());
                row.createCell(1).setCellValue(produto.getQuantidadeVendida());
                
                Cell totalCell = row.createCell(2);
                totalCell.setCellValue(produto.getValorTotal().doubleValue());
                totalCell.setCellStyle(currencyStyle);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel de produtos", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao gerar relatório Excel");
        }
    }
}
