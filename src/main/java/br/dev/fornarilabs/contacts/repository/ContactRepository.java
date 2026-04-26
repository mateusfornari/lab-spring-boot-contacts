package br.dev.fornarilabs.contacts.repository;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByUser(User user, Pageable pageable);
}
