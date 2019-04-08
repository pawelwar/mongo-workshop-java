package wareq.mongoworkshop.repository;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DriverRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DriverRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * This is just an EXAMPLE
     *
     * Get drivers:
     * - with first name Paweł
     * - points greater than or equal 20
     * - sort by points descending
     * - skip first 10 documents and return not more than 100
     */
    public List<Driver> getSomeDrivers() {

        // you can use constants defined in Driver class
        Criteria criteria1 = Criteria
                .where(Driver.FIRST_NAME_FIELD).is("Paweł")
                .and(Driver.POINTS_FIELD).gte(20);

        // or ... write it down as a regular String
        Criteria criteria2 = Criteria
                .where("first_name").is("Paweł")
                .and("points").gte(20);

        return mongoTemplate.find(
                new Query(criteria1)
                        .with(new Sort(Sort.Direction.DESC, "points"))
                        .skip(10)
                        .limit(100),
                Driver.class
        );
    }

    /**
     * Get single driver by id
     */
    public Driver getById(String id) {
        return mongoTemplate.findById(id, Driver.class);
    }

    /**
     * Get drivers with specific first and last name
     */
    public List<Driver> get(String firstName, String lastName) {
        Criteria criteria = Criteria
                .where(Driver.FIRST_NAME_FIELD).is(firstName)
                .and(Driver.LAST_NAME_FIELD).is(lastName);

        return mongoTemplate.find(
                new Query(criteria),
                Driver.class
        );
    }

    /**
     * Get drivers
     * - older than specified age (age parameter)
     * - with pagination (skip and limit parameter)
     * - sorted by age descending
     */
    public List<Driver> getOlderThan(Integer age, Integer skip, Integer limit) {
        Criteria ageGreaterThanCriteria = Criteria.where(Driver.AGE_FIELD).gt(age);
        Sort oldestFirst = new Sort(Sort.Direction.DESC, Driver.AGE_FIELD);

        return mongoTemplate.find(
                new Query(ageGreaterThanCriteria).skip(skip).limit(limit).with(oldestFirst),
                Driver.class
        );
    }

    /**
     * Add new driver to database
     */
    public Driver add(Driver driver) {
        mongoTemplate.save(driver);
        return driver;
    }

    /**
     * Add points to chosen (by id) driver
     */
    public Driver increasePoints(String driverId, int numberOfPointsToAdd) {
        Driver driver = mongoTemplate.findById(driverId, Driver.class);
        if (driver != null) {
            driver.incrementPoints(numberOfPointsToAdd);
            mongoTemplate.save(driver);
        }
        return driver;
    }

    /**
     * Increase by one age of all drivers that are 'currentAge' years old
     */
    public Long increaseAge(Integer currentAge) {
        UpdateResult updateResult = mongoTemplate.updateMulti(
                Query.query(Criteria.where(Driver.AGE_FIELD).is(currentAge)),
                new Update().inc(Driver.AGE_FIELD, 1),
                Driver.class
        );
        return updateResult.getModifiedCount();
    }

}