package com.dusky.world.Module.entity;

public class TimeAxisItem {
    String url;
    String time;
    String num;
    String des;

    public TimeAxisItem(String url, String time, String num, String des) {
        this.url = url;
        this.time = time;
        this.num = num;
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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


}
