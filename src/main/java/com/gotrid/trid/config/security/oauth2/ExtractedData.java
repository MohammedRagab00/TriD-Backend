package com.gotrid.trid.config.security.oauth2;

public record ExtractedData(
        String email,
        String firstName,
        String lastName
) {
}
