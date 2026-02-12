package com.devmaster.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
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
        
        // Pula validação para endpoints públicos
        String requestPath = request.getRequestURI();
        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Map<String, Object> claims = jwtTokenValidator.validateToken(token);
                
                if (claims != null) {
                    // Extrai informações do token
                    String username = (String) claims.get("sub");
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());
                    
                    // Cria authorities
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    
                    // Cria autenticação
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Define no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("Token validado com sucesso para usuário: {}", username);
                }
            }
            
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            log.error("Erro ao processar autenticação JWT: {}", e.getMessage());
            // Delega o tratamento da exceção para o HandlerExceptionResolver
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
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
