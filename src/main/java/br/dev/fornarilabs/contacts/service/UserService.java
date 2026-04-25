package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.User;
import br.dev.fornarilabs.contacts.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            log.warn("User already exists.");
            throw new UserAlreadyExists("User already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User created = userRepository.save(user);
        log.info("User successfully created with ID: {}", created.getId());
        return created;
    }

    public User loadUserById(Long id){
        Optional<User> found = userRepository.findById(id);
        if(found.isEmpty()){
            throw new UserNotFound("User not found by ID: " + id);
        }
        return found.get();
    }
}
