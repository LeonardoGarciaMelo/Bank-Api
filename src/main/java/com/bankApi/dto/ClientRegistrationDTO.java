package com.bankApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for client registration.
 * Ensures that sensitive entity logic is decoupled from the API contract.
 */
public record ClientRegistrationDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "CPF is required")
        @Size(min = 11, max = 14, message = "Invalid CPF format")
        String cpf
) {
}
