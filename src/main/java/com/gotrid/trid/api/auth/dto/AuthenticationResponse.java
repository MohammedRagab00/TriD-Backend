package com.gotrid.trid.api.auth.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record AuthenticationResponse(
        String token,
        String refreshToken,
        Set<String> roles,
        String email,
        String fullName
) {
}
