package com.dusky.world.Module.entity;

import android.service.voice.AlwaysOnHotwordDetector;

import java.util.ArrayList;


/**
 * @AUTHOR: dsy
 * @TIME: 2018/12/11
 * @DESCRIPTION:
 */
public class HomePageData {
    public ArrayList<TooSimple> getTooSimples() {
        return tooSimples;
    }

    public void setTooSimples(ArrayList<TooSimple> tooSimples) {
        this.tooSimples = tooSimples;
    }

    public ArrayList<Fate> getFates() {
        return fates;
    }

    public void setFates(ArrayList<Fate> fates) {
        this.fates = fates;
    }

    public ArrayList<Hot> getHots() {
        return hots;
    }

    public void setHots(ArrayList<Hot> hots) {
        this.hots = hots;
    }

    public ArrayList<DefaultType> getDefaultTypes() {
        return defaultTypes;
    }

    public void setDefaultTypes(ArrayList<DefaultType> defaultTypes) {
        this.defaultTypes = defaultTypes;
    }

    ArrayList<TooSimple> tooSimples;
    ArrayList<Fate> fates;
    ArrayList<Hot> hots;
    ArrayList<DefaultType> defaultTypes;


    public boolean isFateShow() {
        return FateShow;
    }

    public void setFateShow(boolean fateShow) {
        FateShow = fateShow;
    }

    public boolean isHotShow() {
        return HotShow;
    }

    public void setHotShow(boolean hotShow) {
        HotShow = hotShow;
    }

    public boolean isTooSimpleShow() {
        return TooSimpleShow;
    }

    public void setTooSimpleShow(boolean tooSimpleShow) {
        TooSimpleShow = tooSimpleShow;
    }

    boolean FateShow;
    boolean HotShow;
    boolean TooSimpleShow;

    public int getHotPosition() {
        return HotPosition;
    }

    public void setHotPosition(int hotPosition) {
        HotPosition = hotPosition;
    }

    public int getFatePosition() {
        return FatePosition;
    }

    public void setFatePosition(int fatePosition) {
        FatePosition = fatePosition;
    }

    public int getTooSimplePosition() {
        return TooSimplePosition;
    }

    public void setTooSimplePosition(int tooSimplePosition) {
        TooSimplePosition = tooSimplePosition;
    }

    int HotPosition;
    int FatePosition;
    int TooSimplePosition;
}
