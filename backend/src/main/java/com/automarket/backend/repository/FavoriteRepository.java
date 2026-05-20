package com.automarket.backend.repository;

import com.automarket.backend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByUserId(UUID userId);
    Optional<Favorite> findByUserIdAndCarId(UUID userId, UUID carId);
    boolean existsByUserIdAndCarId(UUID userId, UUID carId);
}
