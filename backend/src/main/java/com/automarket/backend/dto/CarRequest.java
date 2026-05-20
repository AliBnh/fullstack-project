package com.automarket.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarRequest {
    @NotBlank
    private String marque;
    @NotBlank
    private String modele;
    @NotNull
    private Integer annee;
    @NotNull
    private Double prix;
    private Integer kilometrage;
    private String carburant;
    private String transmission;
    private String couleur;
    private String description;
    private String localisation;
    private String imageUrl;
}
