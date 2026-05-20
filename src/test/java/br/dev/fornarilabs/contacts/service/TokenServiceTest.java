package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private TokenService tokenService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp(){
        tokenService = new TokenService();

        ReflectionTestUtils.setField(tokenService, "secret", "test-jwt-secret--test-jwt-secret");
        ReflectionTestUtils.setField(tokenService, "userService", userService);
    }

    @Test
    @DisplayName("Must create a JWT token.")
    void mustCreateAJwtToken(){
        User user = new User();
        user.setId(1L);

        String token = tokenService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Must load user by token.")
    void mustLoadUserByToken(){
        User user = new User();
        user.setId(1000L);
        user.setName("Test User");
        user.setEmail("test@user.com");

        String token = tokenService.generateToken(user);

        when(userService.loadUserById(1000L)).thenReturn(user);

        User authUser = tokenService.getUserFromToken(token);
        assertNotNull(authUser);
        assertEquals(user.getEmail(), authUser.getEmail());
        assertEquals(user.getId(), authUser.getId());
        assertEquals(user.getName(), authUser.getName());
    }
}
