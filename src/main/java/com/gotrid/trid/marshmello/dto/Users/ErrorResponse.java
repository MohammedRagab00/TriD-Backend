package com.gotrid.trid.marshmello.dto.Users;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp
) {
    public ErrorResponse(String message) {
        this(message, LocalDateTime.now());
    }
}
