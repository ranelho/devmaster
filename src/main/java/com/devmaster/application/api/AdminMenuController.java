package com.devmaster.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/admin/menu")
@Tag(name = "Menu Admin", description = "Endpoints administrativos de menu - requerem autenticação JWT")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminMenuController {

    @PostMapping
    @Operation(summary = "Criar novo item no menu", description = "Adiciona um novo item ao menu (requer autenticação)")
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody Map<String, Object> item) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "message", "Item criado com sucesso",
            "item", item,
            "createdBy", auth.getName(),
            "timestamp", LocalDateTime.now()
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item do menu", description = "Atualiza um item existente (requer autenticação)")
    public ResponseEntity<Map<String, Object>> updateItem(
            @PathVariable Long id,
            @RequestBody Map<String, Object> item) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "message", "Item atualizado com sucesso",
            "id", id,
            "item", item,
            "updatedBy", auth.getName(),
            "timestamp", LocalDateTime.now()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item do menu", description = "Remove um item do menu (requer autenticação)")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "message", "Item deletado com sucesso",
            "id", id,
            "deletedBy", auth.getName(),
            "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/stats")
    @Operation(summary = "Estatísticas do menu", description = "Retorna estatísticas administrativas (requer autenticação)")
    public ResponseEntity<Map<String, Object>> getStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "totalItems", 42,
            "activeItems", 38,
            "inactiveItems", 4,
            "categories", 5,
            "lastUpdate", LocalDateTime.now(),
            "accessedBy", auth.getName()
        ));
    }
}
