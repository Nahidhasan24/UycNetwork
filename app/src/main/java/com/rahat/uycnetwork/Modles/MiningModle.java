package com.rahat.uycnetwork.Modles;

public class MiningModle {
    private String uid,time,mining;

    public MiningModle() {
    }

    public MiningModle(String uid, String time, String mining) {
        this.uid = uid;
        this.time = time;
        this.mining = mining;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMining() {
        return mining;
    }

    public void setMining(String mining) {
        this.mining = mining;
    }
}
