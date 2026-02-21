package com.devmaster.application.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta genérica para mensagens.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
public class MessageResponse {
    
    private String message;
    
    public MessageResponse(String message) {
        this.message = message;
    }
}
