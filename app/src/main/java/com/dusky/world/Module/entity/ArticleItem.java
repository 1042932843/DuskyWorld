package com.dusky.world.Module.entity;

public class ArticleItem {
    private String title;
    private String img;
    private String summary;
    private String tag;
    private String read_v;
    private String comment_v;
    private String like_v;
    private User user;

    public ArticleItem(String title, String img, String summary, String tag, String read_v, String comment_v, String like_v, User user) {
        this.title = title;
        this.img = img;
        this.summary = summary;
        this.tag = tag;
        this.read_v = read_v;
        this.comment_v = comment_v;
        this.like_v = like_v;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRead_v() {
        return read_v;
    }

    public void setRead_v(String read_v) {
        this.read_v = read_v;
    }

    public String getComment_v() {
        return comment_v;
    }

    public void setComment_v(String comment_v) {
        this.comment_v = comment_v;
    }

    public String getLike_v() {
        return like_v;
    }

    public void setLike_v(String like_v) {
        this.like_v = like_v;
    }
}
