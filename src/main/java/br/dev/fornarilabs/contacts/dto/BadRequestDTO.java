package br.dev.fornarilabs.contacts.dto;

import java.util.List;

public record BadRequestDTO (
        int status,
        String message,
        List<FieldErrorDTO> errors,
        long timestamp
) {
}
