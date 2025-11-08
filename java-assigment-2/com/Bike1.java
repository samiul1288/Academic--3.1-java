package com;

public class Bike1 {
    private final String color;
    private final int year;

    public Bike1(String color, int year) {
        this.color = color;
        this.year = year;
    }

    public String getColor() { return color; }
    public int getYear() { return year; }

    public void bikeStatus() {
        System.out.println("Bike Status: Running!");
    }
}
