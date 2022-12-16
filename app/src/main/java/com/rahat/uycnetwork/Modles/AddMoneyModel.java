package com.rahat.uycnetwork.Modles;

public class AddMoneyModel {
    private String name,phone,method,time,uid,tranid;
    private int amount;

    public AddMoneyModel() {
    }

    public AddMoneyModel(String name, String phone, String method, String time, String uid, String tranid, int amount) {
        this.name = name;
        this.phone = phone;
        this.method = method;
        this.time = time;
        this.uid = uid;
        this.tranid = tranid;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
