package com.rahat.uycnetwork.Modles;

public class UserModle {
    private String uid,name,mail,phone;

    public UserModle() {
    }

    public UserModle(String uid, String name, String mail, String phone) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
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
}
