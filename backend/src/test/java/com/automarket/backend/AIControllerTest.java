package com.automarket.backend;

import com.automarket.backend.dto.AuthResponse;
import com.automarket.backend.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AIControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("ai_" + System.nanoTime() + "@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("AI");
        reg.setLastName("Tester");
        MvcResult r = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg))).andReturn();
        token = mapper.readValue(r.getResponse().getContentAsString(), AuthResponse.class).getToken();
    }

    @Test
    void aiEndpointsRequireAuth() throws Exception {
        mvc.perform(post("/api/ai/chat").contentType(MediaType.APPLICATION_JSON).content("{\"message\":\"test\"}"))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/ai/estimate-price").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/ai/history"))
                .andExpect(status().isForbidden());
    }

    @Test
    void aiHistoryEmpty() throws Exception {
        mvc.perform(get("/api/ai/history").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
