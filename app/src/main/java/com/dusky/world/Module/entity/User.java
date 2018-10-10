package com.dusky.world.Module.entity;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/9/30
 * @DESCRIPTION:
 */
public class User {
    public User(String nickname, String id, String avatar, String level) {
        this.nickname = nickname;
        this.id = id;
        this.avatar = avatar;
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    String nickname;
    String id;
    String avatar;
    String level;
}
