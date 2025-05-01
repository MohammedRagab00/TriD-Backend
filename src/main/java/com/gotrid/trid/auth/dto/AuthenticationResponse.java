package com.gotrid.trid.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        String refreshToken
) {
}
