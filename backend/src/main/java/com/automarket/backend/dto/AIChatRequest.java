package com.automarket.backend.dto;

import lombok.Data;

@Data
public class AIChatRequest {
    private String message;
    private String carId; // optional, for car-specific questions
}
