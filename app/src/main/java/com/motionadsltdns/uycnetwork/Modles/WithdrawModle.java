package com.motionadsltdns.uycnetwork.Modles;

public class WithdrawModle {
    private String name,mail,address,time,type,status,method,uid;
    private int coin;

    public WithdrawModle() {
    }

    public WithdrawModle(String name, String mail, String address, String time, String type, String status, String method, String uid, int coin) {
        this.name = name;
        this.mail = mail;
        this.address = address;
        this.time = time;
        this.type = type;
        this.status = status;
        this.method = method;
        this.uid = uid;
        this.coin = coin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
