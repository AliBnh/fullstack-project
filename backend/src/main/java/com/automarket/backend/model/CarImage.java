package com.automarket.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "car_images")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CarImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(nullable = false)
    private String imageUrl;

    @Builder.Default
    private Boolean isPrimary = false;
}
