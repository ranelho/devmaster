package com.devmaster.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
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
import java.util.*;

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
                    // API Auth retorna 'username', mas também aceita 'sub' como fallback
                    String username = (String) claims.getOrDefault("username", claims.get("sub"));
                    String userId = (String) claims.get("userId");
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());
                    
                    log.info("Claims extraídos do token - username: {}, userId: {}, roles: {}", username, userId, roles);
                    
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
                    
                    // Se temos userId e não existe header X-User-Id, adiciona via wrapper
                    if (userId != null && request.getHeader("X-User-Id") == null) {
                        log.info("Adicionando X-User-Id via wrapper: {}", userId);
                        HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
                            @Override
                            public String getHeader(String name) {
                                if ("X-User-Id".equals(name)) {
                                    return userId;
                                }
                                return super.getHeader(name);
                            }
                            
                            @Override
                            public Enumeration<String> getHeaders(String name) {
                                if ("X-User-Id".equals(name)) {
                                    return Collections.enumeration(Collections.singletonList(userId));
                                }
                                return super.getHeaders(name);
                            }
                            
                            @Override
                            public Enumeration<String> getHeaderNames() {
                                Set<String> names = new HashSet<>();
                                Enumeration<String> originalNames = super.getHeaderNames();
                                while (originalNames.hasMoreElements()) {
                                    names.add(originalNames.nextElement());
                                }
                                names.add("X-User-Id");
                                return Collections.enumeration(names);
                            }
                        };
                        
                        filterChain.doFilter(wrappedRequest, response);
                        return;
                    } else if (userId == null) {
                        log.warn("Token não contém userId. Você precisa fazer logout e login novamente após reiniciar o Auth Service.");
                    }
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
