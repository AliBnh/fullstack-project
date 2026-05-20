package com.automarket.backend;

import com.automarket.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtServiceTest {

    @Autowired private JwtService jwtService;

    @Test
    void generateAndValidateToken() {
        String token = jwtService.generateToken("user@test.com", "BUYER");
        assertThat(token).isNotBlank();
        assertThat(jwtService.isValid(token)).isTrue();
        assertThat(jwtService.extractEmail(token)).isEqualTo("user@test.com");
    }

    @Test
    void invalidToken() {
        assertThat(jwtService.isValid("invalid.token.here")).isFalse();
    }
}
