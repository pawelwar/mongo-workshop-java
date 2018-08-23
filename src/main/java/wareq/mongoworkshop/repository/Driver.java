package wareq.mongoworkshop.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@CompoundIndexes(
        @CompoundIndex(def = "{'" + Driver.FIRST_NAME_FIELD + ": 1, " + Driver.LAST_NAME_FIELD + "' : 1}", background = true)
)
@Document(collection = "drivers")
public class Driver {

    final static String FIRST_NAME_FIELD = "first_name";
    final static String LAST_NAME_FIELD = "last_name";
    final static String POINTS_FIELD = "points";
    final static String AGE_FIELD = "age";
    final static String VEHICLES_FIELD = "vehicles";

    @Id
    private String id;

    @Field(FIRST_NAME_FIELD)
    private String firstName;

    @Field(LAST_NAME_FIELD)
    private String lastName;

    @Indexed(background = true)
    @Field(POINTS_FIELD)
    private Integer points;

    @Indexed(background = true)
    @Field(AGE_FIELD)
    private Integer age;

    @Field(VEHICLES_FIELD)
    private List<Vehicle> vehicles;

    public Driver() {
    }

    public Driver(String id, String firstName, String lastName, Integer points, Integer age, List<Vehicle> vehicles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = points;
        this.age = age;
        this.vehicles = vehicles;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getAge() {
        return age;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", points=" + points +
                ", age=" + age +
                ", vehicles=" + vehicles +
                '}';
    }
}
