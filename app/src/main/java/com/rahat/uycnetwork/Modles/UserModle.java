package com.rahat.uycnetwork.Modles;

public class UserModle {
    private String uid,name,mail,phone,status,refercode,referuse;
    private int coin,refercount;

    public UserModle() {
    }

    public UserModle(String uid, String name, String mail, String phone, String status, String refercode, String referuse, int coin, int refercount) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.status = status;
        this.refercode = refercode;
        this.referuse = referuse;
        this.coin = coin;
        this.refercount = refercount;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefercode() {
        return refercode;
    }

    public void setRefercode(String refercode) {
        this.refercode = refercode;
    }

    public String getReferuse() {
        return referuse;
    }

    public void setReferuse(String referuse) {
        this.referuse = referuse;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getRefercount() {
        return refercount;
    }

    public void setRefercount(int refercount) {
        this.refercount = refercount;
    }
}
