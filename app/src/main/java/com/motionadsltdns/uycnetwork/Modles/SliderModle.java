package com.motionadsltdns.uycnetwork.Modles;

public class SliderModle {
    private String url,image;

    public SliderModle() {
    }

    public SliderModle(String url, String image) {
        this.url = url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
