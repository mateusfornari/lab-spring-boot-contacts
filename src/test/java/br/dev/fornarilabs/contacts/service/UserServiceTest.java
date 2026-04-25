package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_PASSWORD_HASH = "password_hash";
    private static final String TEST_USER_EMAIL = "test@user.com";

    @Test
    @DisplayName("Must save user successfully and generate password hash.")
    void mustSuccessfullySaveUser(){
        User user = new User();
        user.setName("Test User");
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_PASSWORD);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(TEST_PASSWORD_HASH);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);

        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(userRepository, times(1)).existsByEmail(TEST_USER_EMAIL);
        verify(userRepository, times(1)).save(user);

    }

    @Test
    @DisplayName("Must throw UserAlreadyExists exception.")
    void mustThrowUserAlreadyExists(){
        User user = new User();
        user.setEmail(TEST_USER_EMAIL);

        when(userRepository.existsByEmail(TEST_USER_EMAIL)).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> {
            userService.save(user);
        });

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

}
