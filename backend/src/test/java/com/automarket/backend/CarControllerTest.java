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
class CarControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("seller_" + System.nanoTime() + "@test.com");
        reg.setPassword("pass123");
        reg.setFirstName("Seller");
        reg.setLastName("Test");

        MvcResult result = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg)))
                .andReturn();
        AuthResponse auth = mapper.readValue(result.getResponse().getContentAsString(), AuthResponse.class);
        token = auth.getToken();
    }

    @Test
    void createAndGetCar() throws Exception {
        CarRequest car = new CarRequest();
        car.setMarque("TestBrand");
        car.setModele("TestModel");
        car.setAnnee(2022);
        car.setPrix(150000.0);
        car.setKilometrage(20000);
        car.setLocalisation("Casablanca");

        MvcResult result = mvc.perform(post("/api/cars").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marque").value("TestBrand"))
                .andReturn();

        String id = mapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mvc.perform(get("/api/cars/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modele").value("TestModel"));
    }

    @Test
    void listCarsPublic() throws Exception {
        mvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void createCarWithoutAuth() throws Exception {
        CarRequest car = new CarRequest();
        car.setMarque("X");
        car.setModele("Y");
        car.setAnnee(2020);
        car.setPrix(100000.0);

        mvc.perform(post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(car)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteCar() throws Exception {
        CarRequest car = new CarRequest();
        car.setMarque("ToDelete");
        car.setModele("Car");
        car.setAnnee(2020);
        car.setPrix(50000.0);

        MvcResult result = mvc.perform(post("/api/cars").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(car)))
                .andReturn();
        String id = mapper.readTree(result.getResponse().getContentAsString()).get("id").asText();

        mvc.perform(delete("/api/cars/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mvc.perform(get("/api/cars/" + id))
                .andExpect(status().isNotFound());
    }
}
