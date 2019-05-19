package com.example.elf.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.example.elf.ui.activities.SongDetailActivity;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final boolean keepTrue = true;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    /**
     * 类MyBinder继承Binder实现接口IService
     */

    private class MyBinder extends Binder implements IService {
        //2. 定义类MyBinder继承Binder实现接口IService中的函数
        @Override
        public void callPlayMusic(String path) {
            playMusic(path);
        }

        @Override
        public void callPauseMusic() {
            pauseMusic();
        }

        @Override
        public void callConMusic() {
            conMusic();
        }

        @Override
        public void callSeekToPos(int pos) {
            seekToPos(pos);
        }


    }

    /**
     * 继续播放音乐
     */
    private void conMusic() {
        mediaPlayer.start();
    }

    /**
     * 暂停播放
     */
    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 播放音乐
     *
     * @param path
     */
    private void playMusic(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
            updateSeekBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新进度条
     */
    private void updateSeekBar() {

        new Thread() {
            @Override
            public void run() {
                while (keepTrue) {
                    try {
                        Thread.sleep(1000);
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        //获取总时长
                        int duration = mediaPlayer.getDuration();
                        //发送数据给activity
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentPosition", currentPosition);
                        message.setData(bundle);
                        SongDetailActivity.handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 设置seekBar位置
     *
     * @param pos
     */
    private void seekToPos(int pos) {
        mediaPlayer.seekTo(pos);
    }
}
