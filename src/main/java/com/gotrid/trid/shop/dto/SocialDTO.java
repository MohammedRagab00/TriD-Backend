package com.gotrid.trid.shop.dto;

import jakarta.validation.constraints.NotBlank;

public record SocialDTO(
        @NotBlank(message = "Platform is required")
        String platform,
        @NotBlank(message = "Link is required")
        String link
) {
}
