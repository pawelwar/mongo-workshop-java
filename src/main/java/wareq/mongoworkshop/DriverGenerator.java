package wareq.mongoworkshop;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import wareq.mongoworkshop.repository.Driver;
import wareq.mongoworkshop.repository.Vehicle;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DriverGenerator {

    private static final Random random = new Random();
    private static final Set<String> firstNames = Sets.newHashSet(
            "Paweł", "Ania", "Michał", "Józef", "Emi", "Korneliusz", "Aneta",
            "Julia", "Zofia", "Hanna", "Oliwia", "Rita", "Justyna", "Filip",
            "Wojciech", "Szymon", "Jakub", "Antoni", "Jan", "Brajan", "Diana",
            "Marta", "Juri", "Bartosz", "Tymoteusz", "Grzegorz", "Robert",
            "Katarzyna", "Michał", "Stanisław", "Paweł", "Dariusz", "Bogumił",
            "Marek", "Kamil", "Leszek", "Rafał");

    private static final Set<String> lastNames = Sets.newHashSet(
            "Nowak", "Miller", "Marcinkiewicz", "Krakus", "Michalik", "Koza",
            "Kuzera", "Adamek", "Kocur", "Dzban", "Smok", "Trajs", "Machoń",
            "Prako", "Bral", "Galak", "Kaszkowiak", "Niśkiewicz", "Mrok",
            "Wron", "Fiołek", "Nak", "Antokowiak", "Orłoś", "Trój", "Góra",
            "Zawodnik", "Gajos", "Pazura", "Lars"
    );

    private static final Set<String> areaCodeParts = Sets.newHashSet(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "R", "S", "T", "U", "W", "Z", "X"
    );

    private static final Set<String> colors = Sets.newHashSet(
            "blue", "red", "pink", "black", "yellow", "green", "white"
    );

    public Driver generate() {
        return new Driver(
                null,
                randomFirstName(),
                randomLastName(),
                randomPoints(),
                randomAge(),
                randomVehicles()
        );
    }

    private String randomFirstName() {
        return getRandom(firstNames);
    }

    private String randomLastName() {
        return getRandom(lastNames);
    }

    private Integer randomPoints() {
        return random.nextInt(100);
    }

    private Integer randomAge() {
        return random.nextInt(85) + 15;
    }

    private List<Vehicle> randomVehicles() {
        int numberOrVehicles = random.nextInt(9) + 1;
        return IntStream.range(0, numberOrVehicles)
                .boxed()
                .map(it -> randomVehicle())
                .collect(Collectors.toList());
    }

    private Vehicle randomVehicle() {
        return new Vehicle(
                randomRegistrationNumber(),
                randomColor(),
                randomSeatsNumber()
        );
    }

    private String randomRegistrationNumber() {
        return getRandom(areaCodeParts)
                + random.nextInt(999)
                + Instant.now().getEpochSecond();

    }

    private String randomColor() {
        return getRandom(colors);
    }

    private Integer randomSeatsNumber() {
        return random.nextInt(8) + 2;
    }

    private <E> E getRandom(Collection<E> e) {
        return e.stream()
                .skip((int) (e.size() * Math.random()))
                .findFirst().orElse(null);
    }
}
