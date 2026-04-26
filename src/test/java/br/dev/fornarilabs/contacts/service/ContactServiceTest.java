package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.repository.ContactRepository;
import br.dev.fornarilabs.contacts.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    private static final String TEST_CONTACT_NAME = "Test Contact";

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    @Test
    @DisplayName("Must save and return a contact.")
    void mustSaveAndReturnAContact(){
        User user = new User();
        user.setId(1L);

        Contact contact = new Contact();
        contact.setName(TEST_CONTACT_NAME);
        contact.setUser(user);

        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        Contact created = contactService.save(contact);

        assertNotNull(created);
    }

    @Test
    @DisplayName("Must return a page with contacts.")
    void mustReturnAPageWithContacts(){
        User user = new User();
        user.setId(1L);

        Contact contact = new Contact();
        contact.setName(TEST_CONTACT_NAME);

        List<Contact> contactList = List.of(contact);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Contact> page = new PageImpl<>(contactList, pageable, contactList.size());

        when(userRepository.getReferenceById(any(Long.class))).thenReturn(user);
        when(contactRepository.findByUser(any(User.class), any(Pageable.class))).thenReturn(page);

        Page<Contact> result = contactService.listUserContacts(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TEST_CONTACT_NAME, result.getContent().getFirst().getName());
    }

}
