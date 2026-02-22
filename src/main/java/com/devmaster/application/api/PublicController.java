package com.devmaster.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/public")
@Tag(name = "Public", description = "Endpoints públicos que não requerem autenticação")
public class PublicController {

    @GetMapping("/health")
    @Operation(summary = "Health check público", description = "Verifica se a API está funcionando")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "message", "API está funcionando corretamente"
        ));
    }

    @GetMapping("/info")
    @Operation(summary = "Informações da API", description = "Retorna informações básicas da API")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
            "name", "DevMaster API",
            "version", "1.0.0",
            "description", "API de exemplo com Spring Security e JWT",
            "timestamp", LocalDateTime.now()
        ));
    }
}
