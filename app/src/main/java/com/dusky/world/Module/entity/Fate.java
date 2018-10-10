package com.dusky.world.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/9/30
 * @DESCRIPTION:
 */
public class Fate {
    public Fate(String imgUrl, String title, String expNum) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.expNum = expNum;
    }

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

    String imgUrl;
    String title;
    String expNum;
}
