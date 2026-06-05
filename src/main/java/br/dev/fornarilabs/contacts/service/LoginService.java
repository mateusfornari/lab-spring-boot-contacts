package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentials("User not found by email: " + email));
        if(passwordEncoder.matches(password, user.getPassword())){
            log.info("User {} authorized.", user.getId());
            return tokenService.generateToken(user);
        }
        throw new InvalidCredentials("Invalid password for user: " + user.getId());
    }
}
