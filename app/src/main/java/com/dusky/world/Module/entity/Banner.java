package com.dusky.world.Module.entity;

public class Banner {
    public Banner(String id, String imgUrl, String videoUrl, String musicUrl, String context) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.musicUrl = musicUrl;
        this.context = context;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    private String id;
    private String imgUrl;
    private String videoUrl;
    private String musicUrl;
    private String context;
}
