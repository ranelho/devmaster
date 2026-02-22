package com.devmaster.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/menu")
@Tag(name = "Menu Público", description = "Endpoints públicos de menu - não requerem autenticação")
public class MenuController {

    @GetMapping
    @Operation(summary = "Listar todos os itens do menu", description = "Retorna todos os itens disponíveis no menu")
    public ResponseEntity<Map<String, Object>> getAllItems() {
        return ResponseEntity.ok(Map.of(
            "message", "Menu público - sem autenticação",
            "items", List.of(
                Map.of("id", 1, "name", "Pizza Margherita", "price", 35.90),
                Map.of("id", 2, "name", "Hamburguer Clássico", "price", 28.50),
                Map.of("id", 3, "name", "Salada Caesar", "price", 22.00),
                Map.of("id", 4, "name", "Refrigerante", "price", 8.00)
            )
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna detalhes de um item específico do menu")
    public ResponseEntity<Map<String, Object>> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of(
            "id", id,
            "name", "Pizza Margherita",
            "description", "Pizza tradicional com molho de tomate, mussarela e manjericão",
            "price", 35.90,
            "category", "Pizzas",
            "available", true
        ));
    }

    @GetMapping("/categories")
    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias do menu")
    public ResponseEntity<Map<String, Object>> getCategories() {
        return ResponseEntity.ok(Map.of(
            "categories", List.of("Pizzas", "Hambúrgueres", "Saladas", "Bebidas", "Sobremesas")
        ));
    }
}
