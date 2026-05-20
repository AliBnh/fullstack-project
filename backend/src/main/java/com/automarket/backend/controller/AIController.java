package com.automarket.backend.controller;

import com.automarket.backend.dto.AIChatRequest;
import com.automarket.backend.dto.PriceEstimationRequest;
import com.automarket.backend.model.AIChat;
import com.automarket.backend.model.Car;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.AIChatRepository;
import com.automarket.backend.repository.CarRepository;
import com.automarket.backend.service.GroqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final GroqService groqService;
    private final CarRepository carRepository;
    private final AIChatRepository aiChatRepository;

    @PostMapping("/estimate-price")
    public ResponseEntity<?> estimatePrice(@AuthenticationPrincipal User user, @RequestBody PriceEstimationRequest req) {
        String prompt = "You are a car pricing expert for the Moroccan market. Estimate the fair market price in MAD (Moroccan Dirhams). " +
                "Return a JSON with fields: minPrice, maxPrice, averagePrice, confidence (low/medium/high), reasoning (1-2 sentences).";
        String userMsg = String.format("Brand: %s, Model: %s, Year: %d, Mileage: %d km, Fuel: %s, Transmission: %s, City: %s",
                req.getMarque(), req.getModele(), req.getAnnee(), req.getKilometrage(),
                req.getCarburant(), req.getTransmission(), req.getLocalisation());

        String response = groqService.chat(prompt, userMsg);
        aiChatRepository.save(AIChat.builder().user(user).userMessage(userMsg).aiResponse(response).intentType(AIChat.IntentType.PRICE_ESTIMATION).build());
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@AuthenticationPrincipal User user, @RequestBody AIChatRequest req) {
        String systemPrompt;
        Car car = null;

        if (req.getCarId() != null) {
            car = carRepository.findById(UUID.fromString(req.getCarId())).orElse(null);
            if (car != null) {
                systemPrompt = String.format("You are a helpful car advisor. Answer questions about this car listing: " +
                        "Brand: %s, Model: %s, Year: %d, Price: %.0f MAD, Mileage: %d km, Fuel: %s, Transmission: %s, Color: %s, City: %s. Description: %s",
                        car.getMarque(), car.getModele(), car.getAnnee(), car.getPrix(),
                        car.getKilometrage(), car.getCarburant(), car.getTransmission(),
                        car.getCouleur(), car.getLocalisation(), car.getDescription());
            } else {
                systemPrompt = "You are a helpful car marketplace assistant. Help users with car-related questions.";
            }
        } else {
            systemPrompt = "You are a helpful car marketplace assistant. Help users with car-related questions about buying, selling, maintenance, and pricing in Morocco.";
        }

        String response = groqService.chat(systemPrompt, req.getMessage());
        aiChatRepository.save(AIChat.builder().user(user).car(car).userMessage(req.getMessage()).aiResponse(response).intentType(AIChat.IntentType.QUESTION).build());
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@AuthenticationPrincipal User user, @RequestBody PriceEstimationRequest req) {
        String prompt = "You are a car listing copywriter. Write a compelling, professional car listing description in French (2-3 paragraphs). Be specific and highlight selling points.";
        String userMsg = String.format("Brand: %s, Model: %s, Year: %d, Mileage: %d km, Fuel: %s, Transmission: %s, City: %s",
                req.getMarque(), req.getModele(), req.getAnnee(), req.getKilometrage(),
                req.getCarburant(), req.getTransmission(), req.getLocalisation());

        String response = groqService.chat(prompt, userMsg);
        aiChatRepository.save(AIChat.builder().user(user).userMessage(userMsg).aiResponse(response).intentType(AIChat.IntentType.DESCRIPTION).build());
        return ResponseEntity.ok(Map.of("response", response));
    }

    @GetMapping("/history")
    public ResponseEntity<?> history(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(aiChatRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
    }
}
