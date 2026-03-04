package com.devmaster.config;

import com.devmaster.security.JwtAuthenticationEntryPoint;
import com.devmaster.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // Habilita CORS usando configuração do WebConfig
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos - não requerem autenticação
                // Inclui: /public/auth/login, /public/auth/logout, /public/health, etc.
                // EXCEÇÃO: Endpoints de clientes que requerem autenticação
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/public/v1/clientes", "/public/v2/clientes").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/public/v1/clientes/*", "/public/v2/clientes/*").authenticated()
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/public/v1/clientes/*/reativar", "/public/v2/clientes/*/reativar").authenticated()
                
                // Endereços: POST (adicionar) é permitido sem login, mas outras operações requerem autenticação
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/public/v1/clientes/*/enderecos", "/public/v2/clientes/*/enderecos").permitAll()
                .requestMatchers("/public/v1/clientes/*/enderecos/**", "/public/v2/clientes/*/enderecos/**").authenticated()
                
                .requestMatchers("/public/**").permitAll()
                
                // Endpoints públicos de cupons
                .requestMatchers("/v1/cupons/public/**").permitAll()
                
                // Swagger e documentação
                .requestMatchers(
                    "/api-docs/**",
                    "/swagger/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                
                // Actuator
                .requestMatchers("/actuator/**").permitAll()
                
                // Entregadores - qualquer usuário autenticado pode acessar
                .requestMatchers("/v1/entregadores/**").authenticated()
                .requestMatchers("/v1/entregador-restaurante/**").authenticated()
                
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
