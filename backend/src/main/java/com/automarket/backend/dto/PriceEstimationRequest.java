package com.automarket.backend.dto;

import lombok.Data;

@Data
public class PriceEstimationRequest {
    private String marque;
    private String modele;
    private Integer annee;
    private Integer kilometrage;
    private String carburant;
    private String transmission;
    private String localisation;
}
