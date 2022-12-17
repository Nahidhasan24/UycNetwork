package com.rahat.uycnetwork.Modles;

public class PackagesModle {
    private String name;
    private int price;
    private double speed;

    public PackagesModle() {
    }

    public PackagesModle(String name, int price, double speed) {
        this.name = name;
        this.price = price;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
