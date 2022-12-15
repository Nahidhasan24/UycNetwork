package com.rahat.uycnetwork.Modles;

public class Config {
    private String plan,server,withdraw;
    private int minwithdraw;
    private double rate;

    public Config() {
    }

    public Config(String plan, String server, String withdraw, int minwithdraw, double rate) {
        this.plan = plan;
        this.server = server;
        this.withdraw = withdraw;
        this.minwithdraw = minwithdraw;
        this.rate = rate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public int getMinwithdraw() {
        return minwithdraw;
    }

    public void setMinwithdraw(int minwithdraw) {
        this.minwithdraw = minwithdraw;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
