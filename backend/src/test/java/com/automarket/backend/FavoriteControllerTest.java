package com.automarket.backend;

import com.automarket.backend.dto.AuthResponse;
import com.automarket.backend.dto.CarRequest;
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
class FavoriteControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    private String token;
    private String carId;

    @BeforeEach
    void setup() throws Exception {
        // Register user
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("fav_" + System.nanoTime() + "@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("Fav");
        reg.setLastName("User");
        MvcResult r = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg))).andReturn();
        token = mapper.readValue(r.getResponse().getContentAsString(), AuthResponse.class).getToken();

        // Create a car
        CarRequest car = new CarRequest();
        car.setMarque("TestCar");
        car.setModele("FavTest");
        car.setAnnee(2022);
        car.setPrix(100000.0);
        MvcResult cr = mvc.perform(post("/api/cars").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(car))).andReturn();
        carId = mapper.readTree(cr.getResponse().getContentAsString()).get("id").asText();
    }

    @Test
    void addAndListFavorites() throws Exception {
        mvc.perform(post("/api/favorites/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(get("/api/favorites").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marque").value("TestCar"));
    }

    @Test
    void checkFavorite() throws Exception {
        mvc.perform(get("/api/favorites/check/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(false));

        mvc.perform(post("/api/favorites/" + carId).header("Authorization", "Bearer " + token));

        mvc.perform(get("/api/favorites/check/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorited").value(true));
    }

    @Test
    void removeFavorite() throws Exception {
        mvc.perform(post("/api/favorites/" + carId).header("Authorization", "Bearer " + token));
        mvc.perform(delete("/api/favorites/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(get("/api/favorites/check/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.favorited").value(false));
    }

    @Test
    void duplicateFavoriteRejected() throws Exception {
        mvc.perform(post("/api/favorites/" + carId).header("Authorization", "Bearer " + token));
        mvc.perform(post("/api/favorites/" + carId).header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void favoritesRequireAuth() throws Exception {
        mvc.perform(get("/api/favorites"))
                .andExpect(status().isForbidden());
    }
}
