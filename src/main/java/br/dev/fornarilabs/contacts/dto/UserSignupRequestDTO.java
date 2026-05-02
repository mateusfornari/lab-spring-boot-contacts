package br.dev.fornarilabs.contacts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSignupRequestDTO(
        @NotBlank(message = "The name is required.")
        @Size(max = 255, message = "The name must have max 255 characters.")
        String name,

        @NotBlank(message = "The email is required.")
        @Email(message = "The email must be valid.")
        String email,

        @NotBlank(message = "The password is required.")
        @Size(min = 8, message = "The password must have at least 8 characters.")
        String password
) {
}
