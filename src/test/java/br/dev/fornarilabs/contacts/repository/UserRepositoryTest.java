package br.dev.fornarilabs.contacts.repository;

import br.dev.fornarilabs.contacts.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Must persist and find user by email.")
    void mustPersistAndFindUserByEmail(){
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@user.com");
        user.setPassword("test_password");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail(user.getEmail());

        assertTrue(found.isPresent());
        assertEquals(user.getName(), found.get().getName());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getPassword(), found.get().getPassword());
    }

}
