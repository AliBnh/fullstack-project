package com.automarket.backend.dto;

import com.automarket.backend.model.Car;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarResponse {
    private UUID id;
    private String marque;
    private String modele;
    private Integer annee;
    private Double prix;
    private Integer kilometrage;
    private String carburant;
    private String transmission;
    private String couleur;
    private String description;
    private String localisation;
    private String status;
    private Double aiEstimatedPrice;
    private String imageUrl;
    private UUID sellerId;
    private String sellerName;
    private String sellerPhone;
    private LocalDateTime createdAt;

    public static CarResponse from(Car car) {
        CarResponse r = new CarResponse();
        r.setId(car.getId());
        r.setMarque(car.getMarque());
        r.setModele(car.getModele());
        r.setAnnee(car.getAnnee());
        r.setPrix(car.getPrix());
        r.setKilometrage(car.getKilometrage());
        r.setCarburant(car.getCarburant() != null ? car.getCarburant().name() : null);
        r.setTransmission(car.getTransmission() != null ? car.getTransmission().name() : null);
        r.setCouleur(car.getCouleur());
        r.setDescription(car.getDescription());
        r.setLocalisation(car.getLocalisation());
        r.setStatus(car.getStatus().name());
        r.setAiEstimatedPrice(car.getAiEstimatedPrice());
        r.setImageUrl(car.getImageUrl());
        r.setSellerId(car.getSeller().getId());
        r.setSellerName(car.getSeller().getFirstName() + " " + car.getSeller().getLastName());
        r.setSellerPhone(car.getSeller().getPhone());
        r.setCreatedAt(car.getCreatedAt());
        return r;
    }
}
