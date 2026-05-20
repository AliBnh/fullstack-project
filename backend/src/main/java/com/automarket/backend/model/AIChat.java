package com.automarket.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_chats")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AIChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String userMessage;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String aiResponse;

    @Enumerated(EnumType.STRING)
    private IntentType intentType;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum IntentType { PRICE_ESTIMATION, SEARCH, QUESTION, DESCRIPTION }
}
