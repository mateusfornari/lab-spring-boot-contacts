package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.repository.ContactRepository;
import br.dev.fornarilabs.contacts.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact save(Contact contact){
        return contactRepository.save(contact);
    }

    public Page<Contact> listUserContacts(Long userId, int page, int size){
        User user = userRepository.getReferenceById(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return contactRepository.findByUser(user, pageable);
    }
}
