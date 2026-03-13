package com.devmaster.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);
        String requestPath = request.getRequestURI();

        // Pula validação para endpoints públicos APENAS se não houver token
        // Se houver token, tenta validar mesmo sendo rota pública (para suportar autenticação opcional ou mista)
        if (token == null && isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Map<String, Object> claims = jwtTokenValidator.validateToken(token);

                if (claims != null) {
                    // Extrai informações do token
                    // API Auth retorna 'username', mas também aceita 'sub' como fallback
                    String username = (String) claims.getOrDefault("username", claims.get("sub"));
                    String userId = (String) claims.get("userId");
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());

                    //     log.info("Claims extraídos do token - username: {}, userId: {}, roles: {}", username, userId, roles);

                    // Valida se os campos essenciais estão presentes
                    if (username == null || roles == null || roles.isEmpty()) {
                        log.warn("Token inválido ou expirado - campos essenciais ausentes");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // Cria authorities com prefixo ROLE_ se não existir
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    // Cria autenticação
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Define no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Token validado com sucesso para usuário: {} (ID: {})", username, userId);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            //      log.error("Erro ao processar autenticação JWT: {}", e.getMessage());
            // Delega o tratamento da exceção para o HandlerExceptionResolver
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            // Aceita "Bearer token" ou apenas "token"
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return bearerToken;
        }
        return null;
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/public/") ||
                path.contains("/swagger") ||
                path.contains("/api-docs") ||
                path.contains("/actuator");
    }
}
