package com.motionadsltdns.uycnetwork.Modles;

public class PackagesModle {
    private String name;
    private int price;
    private int speed;
    private int time;

    public PackagesModle() {
    }

    public PackagesModle(String name, int price, int speed, int time) {
        this.name = name;
        this.price = price;
        this.speed = speed;
        this.time = time;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
