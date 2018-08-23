package wareq.mongoworkshop


import spock.lang.Specification
import wareq.mongoworkshop.repository.Driver

class DriverGeneratorTest extends Specification {

    DriverGenerator driverGenerator = new DriverGenerator()

    def "should generate new vehicle"() {
        when:
        Driver driver = driverGenerator.generate()

        then:
        driver.firstName != null
        driver.lastName != null
        driver.age >= 15
        driver.points >= 0
        driver.vehicles != null
        driver.vehicles.size() > 0
    }

}
