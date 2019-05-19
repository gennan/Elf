package com.example.elf;

import android.app.Application;

public class songData extends Application {
    private int songStatus;//播放器当前状态
    private String songName;//歌曲名
    private String singerName;//歌手名
    private String picUrl;//图片地址
    private String mood;//心情 因为菜单写不来 还没用到

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getSongStatus() {
        return songStatus;
    }

    public void setSongStatus(int songStatus) {
        this.songStatus = songStatus;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
