package com.devmaster.security.filter;

import com.devmaster.security.service.TokenValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidationService tokenValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.trim().isEmpty()) {
            log.debug("Request sem token de autenticação: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrai o token - aceita com ou sem "Bearer "
            String token = extractToken(authHeader);

            if (tokenValidationService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        "user", null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Token validado com sucesso para: {} {}", request.getMethod(), request.getRequestURI());
            } else {
                log.warn("Token inválido para: {} {}", request.getMethod(), request.getRequestURI());
                sendUnauthorizedResponse(response, "Token inválido ou expirado");
                return;
            }

        } catch (Exception e) {
            log.error("Erro ao processar token: {}", e.getMessage());
            sendUnauthorizedResponse(response, "Erro ao processar token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do header Authorization.
     * Aceita tanto "Bearer token" quanto apenas "token".
     * 
     * @param authHeader Header Authorization
     * @return Token JWT limpo
     */
    private String extractToken(String authHeader) {
        String token = authHeader.trim();
        
        // Remove "Bearer " se presente (case-insensitive)
        if (token.toLowerCase().startsWith("bearer ")) {
            token = token.substring(7).trim();
        }
        
        log.debug("Token extraído (primeiros 20 chars): {}...", 
                  token.length() > 20 ? token.substring(0, 20) : token);
        
        return token;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", message);
        errorResponse.put("status", 401);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
