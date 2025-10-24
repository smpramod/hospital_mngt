package com.example.patientid_2.model;

/**
 * Created by rppatil on 13/12/2017.
 */

public class ItemObject
{
    private int screenShot;
    private String musicName;
    private String musicAuthor;

    public ItemObject(int screenShot, String musicName, String musicAuthor) {
        this.screenShot = screenShot;
        this.musicName = musicName;
        this.musicAuthor = musicAuthor;
    }

    public int getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(int screenShot) {
        this.screenShot = screenShot;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }
}
