package mainpack;

import com.Bike;
import com.Bike1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bike object ...set with setter\n");
        Bike bike = new Bike();
        bike.setYear(2020);
        bike.setColor("red");
        System.out.println("Color: " + bike.getColor());
        System.out.println("Year: " + bike.getYear());
        bike.bikeStatus();

        System.out.println("\nBike1 object ...set with cons\n");
        Bike1 bike1 = new Bike1("Blue", 2018);
        System.out.println("Color: " + bike1.getColor());
        System.out.println("Year: " + bike1.getYear());
        bike1.bikeStatus();
    }
}
