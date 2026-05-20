package com.automarket.backend.controller;

import com.automarket.backend.dto.CarRequest;
import com.automarket.backend.dto.CarResponse;
import com.automarket.backend.model.Car;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.CarRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;

    @GetMapping
    public Page<CarResponse> search(
            @RequestParam(required = false) String marque,
            @RequestParam(required = false) String localisation,
            @RequestParam(required = false) String carburant,
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(required = false) Integer anneeMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Car.Carburant carburantEnum = carburant != null ? Car.Carburant.valueOf(carburant.toUpperCase()) : null;
        return carRepository.search(marque, localisation, carburantEnum, prixMin, prixMax, anneeMin,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(CarResponse::from);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable UUID id) {
        return carRepository.findById(id)
                .map(car -> ResponseEntity.ok(CarResponse.from(car)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CarResponse> create(@AuthenticationPrincipal User user, @Valid @RequestBody CarRequest req) {
        Car car = Car.builder()
                .seller(user)
                .marque(req.getMarque())
                .modele(req.getModele())
                .annee(req.getAnnee())
                .prix(req.getPrix())
                .kilometrage(req.getKilometrage())
                .carburant(req.getCarburant() != null ? Car.Carburant.valueOf(req.getCarburant().toUpperCase()) : null)
                .transmission(req.getTransmission() != null ? Car.Transmission.valueOf(req.getTransmission().toUpperCase()) : null)
                .couleur(req.getCouleur())
                .description(req.getDescription())
                .localisation(req.getLocalisation())
                .imageUrl(req.getImageUrl())
                .build();
        return ResponseEntity.ok(CarResponse.from(carRepository.save(car)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@AuthenticationPrincipal User user, @PathVariable UUID id, @Valid @RequestBody CarRequest req) {
        return carRepository.findById(id)
                .filter(car -> car.getSeller().getId().equals(user.getId()))
                .map(car -> {
                    car.setMarque(req.getMarque());
                    car.setModele(req.getModele());
                    car.setAnnee(req.getAnnee());
                    car.setPrix(req.getPrix());
                    car.setKilometrage(req.getKilometrage());
                    car.setCarburant(req.getCarburant() != null ? Car.Carburant.valueOf(req.getCarburant().toUpperCase()) : null);
                    car.setTransmission(req.getTransmission() != null ? Car.Transmission.valueOf(req.getTransmission().toUpperCase()) : null);
                    car.setCouleur(req.getCouleur());
                    car.setDescription(req.getDescription());
                    car.setLocalisation(req.getLocalisation());
                    car.setImageUrl(req.getImageUrl());
                    return ResponseEntity.ok(CarResponse.from(carRepository.save(car)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return carRepository.findById(id)
                .filter(car -> car.getSeller().getId().equals(user.getId()))
                .map(car -> {
                    carRepository.delete(car);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public Page<CarResponse> myCars(@AuthenticationPrincipal User user,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return carRepository.findBySeller(user, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(CarResponse::from);
    }
}
