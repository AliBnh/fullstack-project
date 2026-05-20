package com.automarket.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cars")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    private Integer annee;
    private Double prix;
    private Integer kilometrage;

    @Enumerated(EnumType.STRING)
    private Carburant carburant;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    private String couleur;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String localisation;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    private Double aiEstimatedPrice;

    private String imageUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Carburant { ESSENCE, DIESEL, ELECTRIQUE, HYBRIDE }
    public enum Transmission { MANUELLE, AUTOMATIQUE }
    public enum Status { DRAFT, ACTIVE, SOLD, ARCHIVED }
}
