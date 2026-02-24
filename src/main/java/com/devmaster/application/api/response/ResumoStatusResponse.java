package com.devmaster.application.api.response;

import com.devmaster.domain.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumoStatusResponse {
    
    private StatusPedido status;
    private Long quantidade;
}
