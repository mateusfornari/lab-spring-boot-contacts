package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.dto.ContactResponseDTO;
import br.dev.fornarilabs.contacts.dto.CreateContactRequestDTO;
import br.dev.fornarilabs.contacts.service.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/contacts")
@Slf4j
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<?> createContact(@AuthenticationPrincipal Object principal, @Valid @RequestBody CreateContactRequestDTO contactData){
        log.info("Contact creation request received.");
        User user = ControllerUtils.getAuthorizedUser(principal);
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setName(contactData.name());
        contact.setEmail(contactData.email());
        contact.setPhoneNumber(contactData.phoneNumber());
        Contact createdContact = contactService.save(contact);
        ContactResponseDTO response = new ContactResponseDTO(createdContact);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listContacts(@AuthenticationPrincipal Object principal, @PageableDefault(page = 0, size = 10) Pageable pageable){
        log.info("List contacts request received.");
        User user = ControllerUtils.getAuthorizedUser(principal);
        Page<Contact> contacts = contactService.listUserContacts(user, pageable.getPageNumber(), pageable.getPageSize());
        Page<ContactResponseDTO> contactsDto = contacts.map(ContactResponseDTO::new);
        return ResponseEntity.ok(contactsDto);
    }

}
