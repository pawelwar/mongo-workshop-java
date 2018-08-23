package wareq.mongoworkshop.endpoint;

import org.springframework.web.bind.annotation.*;
import wareq.mongoworkshop.repository.Driver;
import wareq.mongoworkshop.repository.DriverRepository;

import java.util.List;

@RestController
public class DriverEndpoint {

    private final DriverRepository driverRepository;

    public DriverEndpoint(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
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

    private <TYPE> TYPE getOrDefault(TYPE number, TYPE defaultIfNull) {
        return number == null ? defaultIfNull : number;
    }

}
