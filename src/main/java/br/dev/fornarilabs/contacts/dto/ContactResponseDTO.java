package br.dev.fornarilabs.contacts.dto;

import br.dev.fornarilabs.contacts.domain.Contact;

import java.time.OffsetDateTime;

public record ContactResponseDTO(
        Long id,
        String name,
        String email,
        String phoneNumber,
        OffsetDateTime createdAt
) {
    public ContactResponseDTO(Contact contact){
        this(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhoneNumber(), contact.getCreationTime());
    }
}
