package com.automarket.backend.repository;

import com.automarket.backend.model.Car;
import com.automarket.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    Page<Car> findBySeller(User seller, Pageable pageable);

    @Query("SELECT c FROM Car c WHERE c.status = 'ACTIVE' " +
           "AND (:marque IS NULL OR LOWER(CAST(c.marque AS string)) LIKE LOWER(CONCAT('%',CAST(:marque AS string),'%'))) " +
           "AND (:localisation IS NULL OR LOWER(CAST(c.localisation AS string)) LIKE LOWER(CONCAT('%',CAST(:localisation AS string),'%'))) " +
           "AND (:carburant IS NULL OR c.carburant = :carburant) " +
           "AND (:prixMin IS NULL OR c.prix >= :prixMin) " +
           "AND (:prixMax IS NULL OR c.prix <= :prixMax) " +
           "AND (:anneeMin IS NULL OR c.annee >= :anneeMin)")
    Page<Car> search(@Param("marque") String marque,
                     @Param("localisation") String localisation,
                     @Param("carburant") Car.Carburant carburant,
                     @Param("prixMin") Double prixMin,
                     @Param("prixMax") Double prixMax,
                     @Param("anneeMin") Integer anneeMin,
                     Pageable pageable);
}
