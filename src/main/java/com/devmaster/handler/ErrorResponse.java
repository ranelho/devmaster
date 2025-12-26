package com.devmaster.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 📋 Classe de resposta padronizada para erros da API
 * 
 * Esta classe define a estrutura padrão de todas as respostas de erro
 * da aplicação, garantindo consistência e facilitando o consumo da API.
 * 
 * Funcionalidades:
 * - Estrutura padronizada de erro
 * - Informações detalhadas para debug
 * - Compatibilidade com OpenAPI/Swagger
 * - Serialização JSON otimizada
 * - Campos opcionais para flexibilidade
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padronizada para erros da API")
public class ErrorResponse {

    /**
     * 🕐 Timestamp do erro
     * 
     * Momento exato em que o erro ocorreu, útil para correlação
     * com logs e debugging temporal.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp do erro", example = "2025-12-26 10:30:45")
    private LocalDateTime timestamp;

    /**
     * 🔢 Código de status HTTP
     * 
     * Código numérico do status HTTP (400, 404, 500, etc.)
     * para facilitar o tratamento programático.
     */
    @Schema(description = "Código de status HTTP", example = "400")
    private Integer status;

    /**
     * ⚠️ Tipo do erro
     * 
     * Categoria ou tipo do erro em formato legível,
     * útil para classificação e tratamento específico.
     */
    @Schema(description = "Tipo do erro", example = "Validation Failed")
    private String error;

    /**
     * 💬 Mensagem do erro
     * 
     * Descrição amigável do erro para exibição ao usuário final,
     * em português e com linguagem clara.
     */
    @Schema(description = "Mensagem descritiva do erro", example = "Dados inválidos fornecidos")
    private String message;

    /**
     * 🛣️ Caminho da requisição
     * 
     * URL/endpoint onde o erro ocorreu, útil para identificar
     * a origem do problema.
     */
    @Schema(description = "Caminho da requisição que gerou o erro", example = "/api/users")
    private String path;

    /**
     * 🔄 Método HTTP
     * 
     * Método HTTP usado na requisição (GET, POST, PUT, DELETE, etc.)
     * para contexto completo do erro.
     */
    @Schema(description = "Método HTTP da requisição", example = "POST")
    private String method;

    /**
     * 📋 Detalhes adicionais
     * 
     * Mapa com informações específicas do erro, como:
     * - Campos de validação que falharam
     * - Parâmetros inválidos
     * - Sugestões de correção
     * - Códigos de erro específicos
     * 
     * Este campo é opcional e só aparece quando há detalhes relevantes.
     */
    @Schema(description = "Detalhes específicos do erro (campos de validação, parâmetros, etc.)")
    private Map<String, String> details;

    /**
     * 🆔 ID de rastreamento (opcional)
     * 
     * Identificador único para rastrear o erro nos logs,
     * útil para suporte técnico e debugging.
     */
    @Schema(description = "ID único para rastreamento do erro", example = "ERR-2025-001234")
    private String traceId;

    /**
     * 💡 Sugestão de correção (opcional)
     * 
     * Dica ou sugestão de como corrigir o erro,
     * melhorando a experiência do desenvolvedor.
     */
    @Schema(description = "Sugestão de como corrigir o erro", 
            example = "Verifique se todos os campos obrigatórios foram preenchidos")
    private String suggestion;

    /**
     * 🔗 Link para documentação (opcional)
     * 
     * URL para documentação relevante sobre o erro
     * ou como usar corretamente a API.
     */
    @Schema(description = "Link para documentação relacionada", 
            example = "https://docs.api.com/errors/validation")
    private String documentationUrl;
}