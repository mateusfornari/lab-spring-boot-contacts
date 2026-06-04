package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.config.SecurityConfig;
import br.dev.fornarilabs.contacts.config.SecurityFilter;
import br.dev.fornarilabs.contacts.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@WebMvcTest
@Import(SecurityConfig.class)
public class BaseControllerTest {

    @MockitoBean
    private SecurityFilter securityFilter;

    protected User userMock;

    @BeforeEach
    void setup() throws ServletException, IOException {
        userMock = new User();
        userMock.setId(1L);
        userMock.setName("Test User");
        userMock.setEmail("test@user.com");
        userMock.setPassword("password_hash");
        userMock.setCreationTime(OffsetDateTime.now(ZoneOffset.UTC));

        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain filterChain = invocation.getArgument(2);

            filterChain.doFilter(request, response); // Continua a corrente do Spring
            return null;
        }).when(securityFilter).doFilter(any(), any(), any());
    }

    protected UsernamePasswordAuthenticationToken getAuth(){
        return new UsernamePasswordAuthenticationToken(userMock, null, Collections.emptyList());
    }

}
