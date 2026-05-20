package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.config.SecurityConfig;
import br.dev.fornarilabs.contacts.config.SecurityFilter;
import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.service.UserAlreadyExists;
import br.dev.fornarilabs.contacts.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.mockito.Mockito.doAnswer;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SecurityFilter securityFilter;

    private User userMock;

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

    @Test
    @DisplayName("Must return 201 and the created user data.")
    void mustReturn201AndCreatedUserData() throws Exception {
        Map<String, String> signupRequest = createRequest();

        when(userService.save(any(User.class))).thenReturn(userMock);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userMock.getId()))
                .andExpect(jsonPath("$.name").value(userMock.getName()))
                .andExpect(jsonPath("$.email").value(userMock.getEmail()))
                .andExpect(jsonPath("$.createdAt").value(userMock.getCreationTime().toString()))
                .andExpect(jsonPath("$.password").doesNotExist())
        ;

    }

    @Test
    @DisplayName("Must return 422 when user already exists.")
    void mustReturn422WhenUserAlreadyExists() throws Exception{
        Map<String, String> signupRequest = createRequest();

        when(userService.save(any(User.class))).thenThrow(UserAlreadyExists.class);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    @DisplayName("Must return 400 when payload is invalid.")
    void mustReturn400WhenPayloadIsInvalid() throws Exception{
        String invalidJson = "{\"name\":\"Test User\",\"email\":\"invalid-email\",\"password\":\"123\"}";
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request."))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("email", "password")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("The email must be valid.", "The password must have at least 8 characters.")))
        ;
    }

    @Test
    @DisplayName("Must return 400 when payload is missing fields.")
    void mustReturn400WhenPayloadIsMissingFields() throws Exception{
        String invalidJson = "{\"email\":\"test@user.com\",\"password\":\"test_password\"}";
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request."))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("name")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("The name is required.")))
        ;
    }

    @Test
    @DisplayName("Must return 400 when payload has unexpected field.")
    void mustReturn400WhenPayloadHasUnexpectedField() throws Exception{
        String invalidJson = "{\"name\":\"Test User\",\"email\":\"test@user.com\",\"password\":\"test_password\",\"extra\":\"test-extra\"}";
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request."))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("extra")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("Unexpected field.")))
        ;
    }

    @Test
    @DisplayName("Must return 400 when payload is empty.")
    void mustReturn400WhenPayloadIsEmpty() throws Exception{
        String invalidJson = "";
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad request."))
                .andExpect(jsonPath("$.errors").isEmpty())
        ;
    }

    @Test
    @DisplayName("Must return authorized user data.")
    void mustReturnAuthorizedUserData() throws Exception{
        var auth = new UsernamePasswordAuthenticationToken(userMock, null, Collections.emptyList());
        mockMvc.perform(get("/api/v1/users")
                .with(authentication(auth))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Map<String, String> createRequest(){
        Map<String, String> signupRequest = new HashMap<>();
        signupRequest.put("name", "Test User");
        signupRequest.put("email", "test@user.com");
        signupRequest.put("password", "test_password");
        return signupRequest;
    }
}
