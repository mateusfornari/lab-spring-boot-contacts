package br.dev.fornarilabs.contacts.repository;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

public class ContactRepositoryTest extends BaseRepositoryTest{

    private static final String TEST_CONTACT_NAME = "Test Contact";
    private static final String TEST_CONTACT_EMAIL = "test@contact.com";
    private static final String TEST_CONTACT_PHONE = "48912345678";

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Must save and list a contact.")
    void mustSaveAndListAContact(){
        User user = new User();
        user.setName("Test User");
        user.setEmail("test2@user.com");
        user.setPassword("test_password");
        User createdUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setName(TEST_CONTACT_NAME);
        contact.setEmail(TEST_CONTACT_EMAIL);
        contact.setPhoneNumber(TEST_CONTACT_PHONE);
        contact.setUser(createdUser);
        Contact createdContact = contactRepository.save(contact);

        assertNotNull(createdContact);
        assertEquals(TEST_CONTACT_NAME, createdContact.getName());
        assertEquals(TEST_CONTACT_EMAIL, createdContact.getEmail());
        assertEquals(TEST_CONTACT_PHONE, createdContact.getPhoneNumber());

        Pageable pageable = PageRequest.of(0, 10);

        Page<Contact> page = contactRepository.findByUser(user, pageable);

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        Contact item = page.getContent().getFirst();
        assertEquals(TEST_CONTACT_NAME, item.getName());
        assertEquals(TEST_CONTACT_EMAIL, item.getEmail());
        assertEquals(TEST_CONTACT_PHONE, item.getPhoneNumber());
    }
}
