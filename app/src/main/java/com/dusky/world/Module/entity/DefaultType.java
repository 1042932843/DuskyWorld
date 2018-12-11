package com.dusky.world.Module.entity;

public class DefaultType {
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpNum() {
        return expNum;
    }

    public void setExpNum(String expNum) {
        this.expNum = expNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }


    User user;
    String imgUrl;
    String title;
    String expNum;

    public DefaultType(User user, String imgUrl, String title, String expNum, int spanCount) {
        this.user = user;
        this.imgUrl = imgUrl;
        this.title = title;
        this.expNum = expNum;
        this.spanCount = spanCount;
    }

    int spanCount;
}
