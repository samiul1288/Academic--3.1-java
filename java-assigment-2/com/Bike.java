package com;

public class Bike {
    private String color;
    private int year;

    public Bike() {}

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public void bikeStatus() {
        System.out.println("Bike Status: Running!");
    }
}
