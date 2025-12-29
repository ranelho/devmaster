package com.devmaster.handler;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"details", "error", "message", "timestamp", "status"})
public class StandardError {
    private String status;
    private int code;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String uuid;
    private Map<String, String> details;
}
