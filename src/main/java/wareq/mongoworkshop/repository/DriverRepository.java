package wareq.mongoworkshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        // TODO you need to create proper criteria and pass it to Query object
        return mongoTemplate.find(new Query(), Driver.class);
    }

    /**
     * Get drivers
     * - older than 80 years old
     * - sorted by age
     * - with pagination (skip and limit parameter)
     */
    public List<Driver> getOlderThan(Integer age, Integer skip, Integer limit) {
        // TODO you need to create proper criteria and pass it to Query object
        // TODO don't forget to sort, skip and limit result
        return mongoTemplate.find(new Query(), Driver.class);
    }

}