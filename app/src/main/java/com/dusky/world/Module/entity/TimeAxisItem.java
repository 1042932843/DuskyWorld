package com.dusky.world.Module.entity;

public class TimeAxisItem {
    String url;

    public TimeAxisItem(String url, String des) {
        this.url = url;
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    String des;
}
