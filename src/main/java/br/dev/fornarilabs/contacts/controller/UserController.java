package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.dto.UserResponseDTO;
import br.dev.fornarilabs.contacts.dto.UserSignupRequestDTO;
import br.dev.fornarilabs.contacts.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequestDTO userData){
        log.info("User signup request received.");
        User user = new User();
        user.setName(userData.name());
        user.setEmail(userData.email());
        user.setPassword(userData.password());
        User createdUser = userService.save(user);
        UserResponseDTO response = new UserResponseDTO(createdUser.getId(), createdUser.getName(), createdUser.getEmail(), createdUser.getCreationTime());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal Object principal){
        log.info("User data request received.");
        User user = ControllerUtils.getAuthorizedUser(principal);
        log.info("User {} is authorized.", user.getId());
        UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getCreationTime());
        return ResponseEntity.ok(responseDTO);
    }
}
