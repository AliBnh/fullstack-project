package com.automarket.backend;

import com.automarket.backend.dto.LoginRequest;
import com.automarket.backend.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @Test
    void registerAndLogin() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("test@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("Test");
        reg.setLastName("User");

        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        LoginRequest login = new LoginRequest();
        login.setEmail("test@test.com");
        login.setPassword("pass123");

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginWithWrongPassword() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("nonexistent@test.com");
        login.setPassword("wrong");

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerDuplicateEmail() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("dup@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("A");
        reg.setLastName("B");

        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)))
                .andExpect(status().isOk());

        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)))
                .andExpect(status().isBadRequest());
    }
}
