package com.gotrid.trid.shop.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DetailsShopDTO(
        @NotBlank(message = "Name is required")
        String name,
        String category,
        String location,
        @NotBlank(message = "Description is required")
        String description,
        @Email(message = "Email is not well formatted")
        String email,
        String phone,
        @Valid
        List<SocialDTO> socialLinks
) {
}
