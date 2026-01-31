package com.devmaster.security.config;

import com.devmaster.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${security.interceptor.enabled:true}")
    private boolean securityEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("🔒 Configurando Spring Security...");
        log.info("🔑 Security enabled: {}", securityEnabled);
        
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (securityEnabled) {
            log.info("✅ Segurança HABILITADA - Endpoints protegidos requerem token JWT");
            
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/api/swagger/**",
                            "/api/swagger-ui/**",
                            "/api/swagger-ui.html",
                            "/api/api-docs/**",
                            "/api/v3/api-docs/**",
                            "/api/actuator/**",
                            "/api/health/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            );

            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            log.info("🛡️ JWT Authentication Filter adicionado à cadeia de segurança");
        } else {
            log.warn("⚠️ Segurança DESABILITADA - Todos os endpoints são públicos!");
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        log.info("✅ Spring Security configurado com sucesso");
        return http.build();
    }
}
