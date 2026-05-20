package com.automarket.backend.repository;

import com.automarket.backend.model.CarImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CarImageRepository extends JpaRepository<CarImage, UUID> {
    List<CarImage> findByCarId(UUID carId);
}
