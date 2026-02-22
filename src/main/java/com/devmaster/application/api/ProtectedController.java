package com.devmaster.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/protected")
@Tag(name = "Protected", description = "Endpoints protegidos que requerem autenticação JWT")
@SecurityRequirement(name = "Bearer Authentication")
public class ProtectedController {

    @GetMapping("/user")
    @Operation(summary = "Informações do usuário autenticado", description = "Retorna informações do usuário logado")
    public ResponseEntity<Map<String, Object>> userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "username", authentication.getName(),
            "authorities", authentication.getAuthorities(),
            "authenticated", authentication.isAuthenticated(),
            "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/data")
    @Operation(summary = "Dados protegidos", description = "Retorna dados que requerem autenticação")
    public ResponseEntity<Map<String, Object>> protectedData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "message", "Estes são dados protegidos",
            "accessedBy", authentication.getName(),
            "timestamp", LocalDateTime.now(),
            "data", Map.of(
                "item1", "Valor confidencial 1",
                "item2", "Valor confidencial 2"
            )
        ));
    }
}
