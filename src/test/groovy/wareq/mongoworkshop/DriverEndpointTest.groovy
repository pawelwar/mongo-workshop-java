package wareq.mongoworkshop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpMethod
import wareq.mongoworkshop.repository.Driver
import wareq.mongoworkshop.repository.Vehicle
import spock.lang.Specification

@SpringBootTest(properties = "spring.data.mongodb.port=0", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DriverEndpointTest extends Specification {

    @Autowired
    MongoTemplate mongoTemplate

    @Autowired
    TestRestTemplate restTemplate

    def REGULAR_DRIVER = new Driver("5b6df04111561d86bfe96dea", "Michał", "Piwowarkski", 5, 34,
            [new Vehicle("PO1341", "black", 10)]
    )

    def STANISLAW_OLDEST_DRIVER = new Driver("5b6df04111561d86bfe96e05", "Stanisław", "Wron", 4, 100,
            [new Vehicle("ZCB2445", "black", 2), new Vehicle("WG9842", "white", 2)]
    )

    def JAN_OLD_DRIVER = new Driver("5b6df04111561d86bfe96e80", "Jan", "Koza", 2, 99,
            [new Vehicle("EOZ6663", "pink", 2), new Vehicle("JM5776", "white", 5)]
    )

    def JULIA_OLD_DRIVER = new Driver("5b6df04111561d86bfe96e84", "Julia", "Michalik", 0, 98,
            [new Vehicle("DOZAAA", "white", 2), new Vehicle("FDA334", "white", 5)]
    )

    def 'should return single driver by id'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER, JAN_OLD_DRIVER, JULIA_OLD_DRIVER)

        when:
        Driver driver = getSingleDriver('/drivers/5b6df04111561d86bfe96e84')

        then:
        driver != null
        driver.id == '5b6df04111561d86bfe96e84'
    }

    def 'should return drivers with selected first and last name'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER, JAN_OLD_DRIVER, JULIA_OLD_DRIVER)

        when:
        List<Driver> drivers = getDrivers('/drivers?first-name=Jan&last-name=Koza')

        then:
        drivers.size() == 1
        drivers.first().firstName == 'Jan'
        drivers.first().lastName == 'Koza'
    }

    def 'should return a list of oldest drivers sorted by age descending'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER, JAN_OLD_DRIVER, JULIA_OLD_DRIVER)

        when:
        List<Driver> drivers = getDrivers('/elders')

        then: 'we should take three drivers'
        drivers.size() == 3

        and: 'they should be sorted by age; oldest first'
        drivers.age == [100, 99, 98]
    }

    def 'should return a limited by size list of oldest drivers'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER, JAN_OLD_DRIVER, JULIA_OLD_DRIVER)

        when:
        List<Driver> drivers = getDrivers("/elders?limit=${limit}")

        then:
        drivers.size() == expectedListSize

        where:
        limit || expectedListSize
        1     || 1
        2     || 2
        3     || 3
        4     || 3
        5     || 3
    }

    def 'should return a list of oldest drivers without a few first record (were skipped)'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER, JAN_OLD_DRIVER, JULIA_OLD_DRIVER)

        when:
        List<Driver> drivers = getDrivers("/elders?skip=${skip}")

        then:
        drivers.first().age == expectedfirstDriverAge

        where:
        skip || expectedfirstDriverAge
        0    || 100
        1    || 99
        2    || 98
    }

    def 'should add new driver'() {
        when:
        Driver driver = getSingleDriver('/commands/generate-driver')

        then:
        driver != null
        driver.id != null
    }

    def 'should increase number of points for chosen driver'() {
        given:
        ensureDriversExist(REGULAR_DRIVER)

        when:
        Driver driverAfterChange = getSingleDriver("/commands/increase-point/$REGULAR_DRIVER.id")

        then:
        driverAfterChange.points == REGULAR_DRIVER.points + 1
    }

    def 'should not increase number of points when driver does not exist'() {
        when:
        Driver driverAfterChange = getSingleDriver("/commands/increase-point/driver-that-not-exists")

        then:
        driverAfterChange == null
    }

    def 'all drivers at chosen age should be one year older'() {
        given:
        ensureDriversExist(REGULAR_DRIVER, STANISLAW_OLDEST_DRIVER)

        when:
        Integer documentsAffected = restTemplate.getForObject("/commands/increase-age/$REGULAR_DRIVER.age", Integer.class)

        then:
        documentsAffected == 1
        println getDriverFromDB(REGULAR_DRIVER.id)
        getDriverFromDB(REGULAR_DRIVER.id).age == REGULAR_DRIVER.age + 1
    }

    List<Driver> getDrivers(String path) {
        def returnType = new ParameterizedTypeReference<List<Driver>>() {}
        return restTemplate.exchange(path, HttpMethod.GET, null, returnType).body
    }

    Driver getSingleDriver(String path) {
        return restTemplate.exchange(path, HttpMethod.GET, null, Driver.class).body
    }

    Driver getDriverFromDB(String id) {
        return mongoTemplate.findById(id, Driver.class)
    }

    def ensureDriversExist(Driver... drivers) {
        drivers.each {
            mongoTemplate.save(it)
        }
    }

}
