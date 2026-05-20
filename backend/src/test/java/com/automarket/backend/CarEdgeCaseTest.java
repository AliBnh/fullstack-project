package com.automarket.backend;

import com.automarket.backend.dto.AuthResponse;
import com.automarket.backend.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CarEdgeCaseTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    private String registerAndGetToken(String email) throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail(email);
        reg.setPassword("pass123");
        reg.setFirstName("Test");
        reg.setLastName("User");
        MvcResult r = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reg))).andReturn();
        return mapper.readValue(r.getResponse().getContentAsString(), AuthResponse.class).getToken();
    }

    @Test
    void getNonExistentCar() throws Exception {
        mvc.perform(get("/api/cars/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCarWithMissingFields() throws Exception {
        String token = registerAndGetToken("edge1_" + System.nanoTime() + "@test.com");
        mvc.perform(post("/api/cars").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"marque\":\"X\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchWithFilters() throws Exception {
        String token = registerAndGetToken("edge2_" + System.nanoTime() + "@test.com");
        // Create car with specific attributes
        mvc.perform(post("/api/cars").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"marque\":\"UniqueTestBrand\",\"modele\":\"X\",\"annee\":2023,\"prix\":999999,\"localisation\":\"TestCity\",\"carburant\":\"HYBRIDE\"}"));

        // Search by marque
        mvc.perform(get("/api/cars?marque=UniqueTestBrand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // Search by localisation
        mvc.perform(get("/api/cars?localisation=TestCity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // Search by carburant
        mvc.perform(get("/api/cars?carburant=HYBRIDE&marque=UniqueTestBrand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));

        // Search with no results
        mvc.perform(get("/api/cars?marque=NonExistentBrand123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void ownershipEnforcement() throws Exception {
        String owner = registerAndGetToken("owner_" + System.nanoTime() + "@test.com");
        String other = registerAndGetToken("other_" + System.nanoTime() + "@test.com");

        // Owner creates car
        MvcResult cr = mvc.perform(post("/api/cars").header("Authorization", "Bearer " + owner)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"marque\":\"OwnerCar\",\"modele\":\"X\",\"annee\":2022,\"prix\":100000}")).andReturn();
        String carId = mapper.readTree(cr.getResponse().getContentAsString()).get("id").asText();

        // Other user cannot update
        mvc.perform(put("/api/cars/" + carId).header("Authorization", "Bearer " + other)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"marque\":\"Hacked\",\"modele\":\"X\",\"annee\":2022,\"prix\":1}"))
                .andExpect(status().isNotFound());

        // Other user cannot delete
        mvc.perform(delete("/api/cars/" + carId).header("Authorization", "Bearer " + other))
                .andExpect(status().isNotFound());

        // Owner can delete
        mvc.perform(delete("/api/cars/" + carId).header("Authorization", "Bearer " + owner))
                .andExpect(status().isOk());
    }

    @Test
    void paginationWorks() throws Exception {
        mvc.perform(get("/api/cars?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0));
    }
}
