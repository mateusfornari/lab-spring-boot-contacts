package br.dev.fornarilabs.contacts.dto;

public record ErrorResponseDTO(
        int status,
        String message,
        long timestamp
) {
}
