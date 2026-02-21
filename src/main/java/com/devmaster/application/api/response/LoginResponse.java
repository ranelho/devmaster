package com.devmaster.application.api.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    UserInfo user
) {
    @Builder
    public record UserInfo(
        String id,
        String username,
        String email,
        Set<String> roles,
        Long restauranteId
    ) {}
}
