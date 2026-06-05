package br.dev.fornarilabs.contacts.dto;

import br.dev.fornarilabs.contacts.domain.User;

import java.time.OffsetDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        OffsetDateTime createdAt
) {
    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getCreationTime());
    }
}
