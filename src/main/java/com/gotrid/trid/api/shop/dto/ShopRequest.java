package com.gotrid.trid.api.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ShopRequest(
        @NotBlank(message = "Name is required")
        String name,
        String category,
        String location,
        @NotBlank(message = "Description is required")
        String description,
        @Email(message = "Email is not well formatted")
        String email,
        String phone
) {
}
