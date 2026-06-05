package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.domain.Contact;
import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.service.ContactService;
import br.dev.fornarilabs.contacts.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;


@WebMvcTest(ContactController.class)
public class ContactControllerTest extends BaseControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ContactService contactService;

    @Test
    @DisplayName("Must create a contact.")
    void mustCreateAContact() throws Exception{
        Contact contactMock = createContactMock();
        String requestContent = objectMapper.writeValueAsString(createRequest(contactMock));

        when(contactService.save(any(Contact.class))).thenReturn(contactMock);

        mockMvc.perform(post("/api/v1/contacts")
                .with(authentication(getAuth()))
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(contactMock.getId()))
                .andExpect(jsonPath("$.name").value(contactMock.getName()))
                .andExpect(jsonPath("$.email").value(contactMock.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(contactMock.getPhoneNumber()));
    }

    @Test
    @DisplayName("Must list contacts and return 200 OK with paginated data.")
    void mustListContactsWithData() throws Exception {
        Contact contactMock = createContactMock();
        List<Contact> contactList = List.of(contactMock);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Contact> pageMock = new PageImpl<>(contactList, pageable, contactList.size());

        when(contactService.listUserContacts(any(User.class), any(Integer.class), any(Integer.class)))
                .thenReturn(pageMock);

        mockMvc.perform(get("/api/v1/contacts")
                .with(authentication(getAuth()))
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(contactMock.getId()))
                .andExpect(jsonPath("$.content[0].name").value(contactMock.getName()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    private Contact createContactMock(){
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setName("Test Contact");
        contact.setEmail("test@contact.com");
        contact.setPhoneNumber("12345678901");
        return contact;
    }

    private Map<String, String> createRequest(Contact contact){
        Map<String, String> contactRequest = new HashMap<>();
        contactRequest.put("name", contact.getName());
        contactRequest.put("email", contact.getEmail());
        contactRequest.put("phoneNumber", contact.getPhoneNumber());
        return contactRequest;
    }

}
