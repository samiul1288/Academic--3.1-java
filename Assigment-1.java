package com;

public class Car {
    private String color;
    private int year;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void CarStatus()
    {
        System.out.println("Car Status: Running!");

    }




//    public com.Car(String color, int year) {
//        this.color = color;
//        this.year = year;
//    }


}
package com;

public class Car1 {
    private String color;
    private int year;

    public String getColor() {
        return color;
    }

      public int getYear() {
        return year;
    }


    public void CarStatus()
    {
        System.out.println("Car Status: Running!");

    }




    public Car1(String color, int year) {
        this.color = color;
        this.year = year;
    }


}

package mainpack;

import com.Car;
import com.Car1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Car object ...set with setter\n");
        Car car = new Car();
        car.setYear(2020);
        car.setColor("red");
        System.out.println("Color:"+car.getColor());
        System.out.println("Year:"+car.getYear());
        car.CarStatus();

        System.out.println("Car1 object ...set with cons\n");
        Car1 car1 = new Car1("Blue", 2018);
        System.out.println("Color:"+car1.getColor());
        System.out.println("Year:"+car1.getYear());
        car1.CarStatus();


    }
}
