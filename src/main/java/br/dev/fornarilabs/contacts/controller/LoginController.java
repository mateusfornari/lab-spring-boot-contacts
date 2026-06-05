package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.dto.LoginRequestDTO;
import br.dev.fornarilabs.contacts.dto.LoginResponseDTO;
import br.dev.fornarilabs.contacts.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body){
        log.info("Login request received.");
        String token = loginService.login(body.email(), body.password());
        log.info("User authorized: token created.");
        LoginResponseDTO responseDTO = new LoginResponseDTO(token);
        return ResponseEntity.ok(responseDTO);
    }

}
