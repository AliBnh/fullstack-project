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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("upload_" + System.nanoTime() + "@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("Up");
        reg.setLastName("Load");
        MvcResult r = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg))).andReturn();
        token = mapper.readValue(r.getResponse().getContentAsString(), AuthResponse.class).getToken();
    }

    @Test
    void uploadAndServeImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "car.jpg", "image/jpeg", new byte[]{1, 2, 3, 4, 5});

        MvcResult result = mvc.perform(multipart("/api/uploads").file(file).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists())
                .andReturn();

        String url = mapper.readTree(result.getResponse().getContentAsString()).get("url").asText();

        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    void uploadRequiresAuth() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "car.jpg", "image/jpeg", new byte[]{1, 2, 3});
        mvc.perform(multipart("/api/uploads").file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    void serveNonExistentFile() throws Exception {
        mvc.perform(get("/api/uploads/nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }
}
