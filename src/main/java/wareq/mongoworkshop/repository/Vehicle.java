package wareq.mongoworkshop.repository;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

public class Vehicle {

    final static String NUMBER_FIELD = "number";
    final static String COLOR_FIELD = "color";
    final static String SEATS_FIELD = "seats";

    @Indexed(background = true, unique = true)
    @Field(NUMBER_FIELD)
    private String number;

    @Field(COLOR_FIELD)
    private String color;

    @Field(SEATS_FIELD)
    private Integer seats;

    public Vehicle() {
    }

    public Vehicle(String number, String color, Integer seats) {
        this.number = number;
        this.color = color;
        this.seats = seats;
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public Integer getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "number='" + number + '\'' +
                ", color='" + color + '\'' +
                ", seats=" + seats +
                '}';
    }
}
