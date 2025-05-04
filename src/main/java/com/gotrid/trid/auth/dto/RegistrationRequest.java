package com.gotrid.trid.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RegistrationRequest(
        @NotBlank(message = "Firstname is required")
        String firstname,

        @NotBlank(message = "Lastname is required")
        String lastname,

        @Email(message = "Email is not well formatted")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "Password must be at least 8 characters long and contain both letters and numbers")
        String password,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate birthDate,

        @NotBlank(message = "Gender is required")
        String gender
) {
}