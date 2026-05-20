package com.automarket.backend.config;

import com.automarket.backend.model.Car;
import com.automarket.backend.model.User;
import com.automarket.backend.repository.CarRepository;
import com.automarket.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        User ahmed = userRepository.save(User.builder()
                .email("ahmed@automarket.ma").password(passwordEncoder.encode("password123"))
                .firstName("Ahmed").lastName("Benali").phone("0661234567").city("Casablanca").build());

        User karim = userRepository.save(User.builder()
                .email("karim@automarket.ma").password(passwordEncoder.encode("password123"))
                .firstName("Karim").lastName("Tazi").phone("0667891234").city("Rabat").build());

        User omar = userRepository.save(User.builder()
                .email("omar@automarket.ma").password(passwordEncoder.encode("password123"))
                .firstName("Omar").lastName("Fassi").phone("0677112233").city("Marrakech").build());

        userRepository.save(User.builder()
                .email("sara@automarket.ma").password(passwordEncoder.encode("password123"))
                .firstName("Sara").lastName("Alami").phone("0655123456").city("Fes").build());

        carRepository.save(Car.builder().seller(ahmed).marque("Toyota").modele("Corolla").annee(2021).prix(195000.0)
                .kilometrage(32000).carburant(Car.Carburant.ESSENCE).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Blanc").description("Toyota Corolla en excellent etat, premiere main, carnet entretien complet. Climatisation, camera de recul, ecran tactile.")
                .localisation("Casablanca").build());

        carRepository.save(Car.builder().seller(ahmed).marque("Dacia").modele("Duster").annee(2022).prix(230000.0)
                .kilometrage(18000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.MANUELLE)
                .couleur("Noir").description("Dacia Duster 4x4, ideal pour la montagne et la ville. Barres de toit, GPS integre.")
                .localisation("Casablanca").build());

        carRepository.save(Car.builder().seller(ahmed).marque("Volkswagen").modele("Golf 8").annee(2020).prix(280000.0)
                .kilometrage(45000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Gris").description("VW Golf 8 full options, toit ouvrant, camera de recul, sieges chauffants, LED Matrix.")
                .localisation("Casablanca").build());

        carRepository.save(Car.builder().seller(ahmed).marque("Audi").modele("A3 Sportback").annee(2021).prix(320000.0)
                .kilometrage(28000).carburant(Car.Carburant.ESSENCE).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Bleu").description("Audi A3 Sportback S-Line, virtual cockpit, Bang & Olufsen, matrix LED.")
                .localisation("Casablanca").build());

        carRepository.save(Car.builder().seller(karim).marque("Renault").modele("Clio 5").annee(2023).prix(165000.0)
                .kilometrage(8000).carburant(Car.Carburant.ESSENCE).transmission(Car.Transmission.MANUELLE)
                .couleur("Rouge").description("Renault Clio 5 quasi neuve, garantie constructeur encore valide 2 ans.")
                .localisation("Rabat").build());

        carRepository.save(Car.builder().seller(karim).marque("Mercedes").modele("Classe C 220d").annee(2019).prix(350000.0)
                .kilometrage(62000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Noir").description("Mercedes Classe C 220d AMG Line, interieur cuir, GPS, Burmester, toit panoramique.")
                .localisation("Rabat").build());

        carRepository.save(Car.builder().seller(karim).marque("Hyundai").modele("Tucson").annee(2021).prix(270000.0)
                .kilometrage(35000).carburant(Car.Carburant.HYBRIDE).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Bleu").description("Hyundai Tucson Hybrid, economique et spacieux, ideal famille.")
                .localisation("Rabat").build());

        carRepository.save(Car.builder().seller(karim).marque("Peugeot").modele("3008").annee(2022).prix(310000.0)
                .kilometrage(22000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Gris").description("Peugeot 3008 GT Line, i-Cockpit, toit panoramique, Night Vision.")
                .localisation("Rabat").build());

        carRepository.save(Car.builder().seller(omar).marque("BMW").modele("Serie 3 320d").annee(2020).prix(340000.0)
                .kilometrage(48000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Blanc").description("BMW Serie 3 320d M Sport, cuir Dakota, HUD, Harman Kardon.")
                .localisation("Marrakech").build());

        carRepository.save(Car.builder().seller(omar).marque("Range Rover").modele("Evoque").annee(2021).prix(480000.0)
                .kilometrage(30000).carburant(Car.Carburant.DIESEL).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Noir").description("Range Rover Evoque R-Dynamic, cuir, meridian, camera 360.")
                .localisation("Marrakech").build());

        carRepository.save(Car.builder().seller(omar).marque("Fiat").modele("500").annee(2022).prix(145000.0)
                .kilometrage(12000).carburant(Car.Carburant.ESSENCE).transmission(Car.Transmission.MANUELLE)
                .couleur("Blanc").description("Fiat 500 Dolcevita, toit ouvrant, parfaite pour la ville.")
                .localisation("Marrakech").build());

        carRepository.save(Car.builder().seller(omar).marque("Kia").modele("Sportage").annee(2023).prix(330000.0)
                .kilometrage(5000).carburant(Car.Carburant.HYBRIDE).transmission(Car.Transmission.AUTOMATIQUE)
                .couleur("Vert").description("Kia Sportage HEV, quasi neuf, garantie 7 ans, ecran panoramique.")
                .localisation("Tanger").build());

        System.out.println("✅ Database seeded with 4 users and 12 cars");
    }
}
