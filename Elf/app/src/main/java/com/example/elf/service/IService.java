package com.example.elf.service;

public interface IService {
    public void callPlayMusic(String path);


    public void callPauseMusic();


    public void callConMusic();


    public void callSeekToPos(int pos);
}
