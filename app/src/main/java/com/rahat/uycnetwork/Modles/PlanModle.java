package com.rahat.uycnetwork.Modles;

public class PlanModle {
    private String uid,name,date,plan;
    private double speed;


    public PlanModle() {
    }

    public PlanModle(String uid, String name, String date, String plan, double speed) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.plan = plan;
        this.speed = speed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
