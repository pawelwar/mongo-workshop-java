package wareq.mongoworkshop.endpoint;

import org.springframework.web.bind.annotation.*;
import wareq.mongoworkshop.DriverGenerator;
import wareq.mongoworkshop.repository.Driver;
import wareq.mongoworkshop.repository.DriverRepository;

import java.util.List;

@RestController
public class DriverEndpoint {

    private final DriverRepository driverRepository;
    private final DriverGenerator driverGenerator;

    public DriverEndpoint(DriverRepository driverRepository, DriverGenerator driverGenerator) {
        this.driverRepository = driverRepository;
        this.driverGenerator = driverGenerator;
    }

    /**
     * http://localhost:8080/drivers?first-name=Jan&&last-name=Nowak
     */
    @GetMapping("drivers")
    public List<Driver> drivers(
            @RequestParam(name = "first-name") String firstName,
            @RequestParam(name = "last-name") String lastName
    ) {
        return driverRepository.get(firstName, lastName);
    }

    /**
     * http://localhost:8080/drivers/5b6df04211561d86bfe9705f
     */
    @GetMapping("drivers/{id}")
    public Driver drivers(@PathVariable String id) {
        return driverRepository.getById(id);
    }

    /**
     * http://localhost:8080/elders?skip=10&limit=100
     */
    @GetMapping("elders")
    public List<Driver> elders(
            @RequestParam(required = false) Integer skip,
            @RequestParam(required = false) Integer limit
    ) {
        return driverRepository.getOlderThan(
                80,
                getOrDefault(skip, 0),
                getOrDefault(limit, 100)
        );
    }

    // For convenience purpose, GET are used for commands endpoints
    // ...but it is worth noting that it should be a POST (according to REST specification)

    /**
     * http://localhost:8080/commands/generate-driver
     */
    @GetMapping("commands/generate-driver")
    public Driver generate() {
        Driver driver = driverGenerator.generate();
        return driverRepository.add(driver);
    }

    /**
     * http://localhost:8080/commands/increase-point/5b6df04211561d86bfe9705f
     */
    @GetMapping("commands/increase-point/{id}")
    public Driver generate(@PathVariable String id) {
        return driverRepository.increasePoints(id, 1);
    }

    /**
     * http://localhost:8080/commands/increase-age/20
     */
    @GetMapping("commands/increase-age/{currentAge}")
    public Long generate(@PathVariable Integer currentAge) {
        return driverRepository.increaseAge(currentAge);
    }

    private <TYPE> TYPE getOrDefault(TYPE number, TYPE defaultIfNull) {
        return number == null ? defaultIfNull : number;
    }

}
