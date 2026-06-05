package br.dev.fornarilabs.contacts.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateContactRequestDTO(
        @NotBlank(message = "The name is required.")
        @Size(max = 255, message = "The name must have max 255 characters.")
        String name,

        @NotBlank(message = "The email is required.")
        @Email(message = "The email must be valid.")
        String email,

        @NotBlank(message = "The phone number is required.")
        @Digits(integer = 11, fraction = 0, message = "The phone number bust contains up to 11 digits.")
        String phoneNumber
) {
}
