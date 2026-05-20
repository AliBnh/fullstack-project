package com.automarket.backend;

import com.automarket.backend.model.Car;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.CarRepository;
import com.automarket.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CarRepositoryTest {

    @Autowired private CarRepository carRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void searchByMarque() {
        User seller = userRepository.save(User.builder().email("s@t.com").password("x").firstName("A").lastName("B").role(User.Role.USER).build());
        carRepository.save(Car.builder().seller(seller).marque("Toyota").modele("Corolla").annee(2021).prix(190000.0).build());
        carRepository.save(Car.builder().seller(seller).marque("Dacia").modele("Duster").annee(2022).prix(230000.0).build());

        Page<Car> results = carRepository.search("Toyota", null, null, null, null, null, PageRequest.of(0, 10));
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).getMarque()).isEqualTo("Toyota");
    }

    @Test
    void searchByPriceRange() {
        User seller = userRepository.save(User.builder().email("s2@t.com").password("x").firstName("A").lastName("B").role(User.Role.USER).build());
        carRepository.save(Car.builder().seller(seller).marque("A").modele("X").annee(2020).prix(100000.0).build());
        carRepository.save(Car.builder().seller(seller).marque("B").modele("Y").annee(2020).prix(300000.0).build());

        Page<Car> results = carRepository.search(null, null, null, 150000.0, 350000.0, null, PageRequest.of(0, 10));
        assertThat(results.getTotalElements()).isEqualTo(1);
    }

    @Test
    void searchNoFilters() {
        User seller = userRepository.save(User.builder().email("s3@t.com").password("x").firstName("A").lastName("B").role(User.Role.USER).build());
        carRepository.save(Car.builder().seller(seller).marque("X").modele("Y").annee(2020).prix(100000.0).build());

        Page<Car> results = carRepository.search(null, null, null, null, null, null, PageRequest.of(0, 10));
        assertThat(results.getTotalElements()).isGreaterThanOrEqualTo(1);
    }
}
