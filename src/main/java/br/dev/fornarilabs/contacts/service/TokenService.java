package br.dev.fornarilabs.contacts.service;

import br.dev.fornarilabs.contacts.domain.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Autowired
    private UserService userService;

    @Value("${app.security.jwt.secret}")
    private String secret;

    public String generateToken(User user){
        Instant now = Instant.now();
        Instant expiration = now.plus(Duration.ofHours(2));
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public User getUserFromToken(String token){
        try{
            String subject = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            Long userId = Long.parseLong(subject);
            return userService.loadUserById(userId);
        } catch (JwtException | NumberFormatException | UserNotFound e) {
            log.warn("Invalid token: {}", e.getMessage());
            return null;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
