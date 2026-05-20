package com.automarket.backend.controller;

import com.automarket.backend.dto.CarResponse;
import com.automarket.backend.model.Favorite;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.CarRepository;
import com.automarket.backend.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final CarRepository carRepository;

    @GetMapping
    public List<CarResponse> myFavorites(@AuthenticationPrincipal User user) {
        return favoriteRepository.findByUserId(user.getId()).stream()
                .map(fav -> CarResponse.from(fav.getCar()))
                .toList();
    }

    @GetMapping("/check/{carId}")
    public ResponseEntity<?> check(@AuthenticationPrincipal User user, @PathVariable UUID carId) {
        boolean fav = favoriteRepository.existsByUserIdAndCarId(user.getId(), carId);
        return ResponseEntity.ok(java.util.Map.of("favorited", fav));
    }

    @PostMapping("/{carId}")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @PathVariable UUID carId) {
        if (favoriteRepository.existsByUserIdAndCarId(user.getId(), carId)) {
            return ResponseEntity.badRequest().body("Already favorited");
        }
        return carRepository.findById(carId).map(car -> {
            favoriteRepository.save(Favorite.builder().user(user).car(car).build());
            return ResponseEntity.ok("Added to favorites");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<?> remove(@AuthenticationPrincipal User user, @PathVariable UUID carId) {
        return favoriteRepository.findByUserIdAndCarId(user.getId(), carId)
                .map(fav -> {
                    favoriteRepository.delete(fav);
                    return ResponseEntity.ok("Removed from favorites");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
