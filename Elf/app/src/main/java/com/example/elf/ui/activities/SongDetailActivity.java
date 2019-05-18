package com.example.elf.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.elf.R;
import com.example.elf.service.IService;
import com.example.elf.service.MusicService;

public class SongDetailActivity extends AppCompatActivity {
    private static int songStatus = -1;
    private IService iService;
    private static SeekBar progressSb;
    private ImageView playIv;
    private ImageView movebcakIv;
    private ImageView moveForwardIv;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");
            progressSb.setMax(duration);
            progressSb.setProgress(currentPosition);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        //找到控件
        progressSb = findViewById(R.id.sb_detail);
        playIv = findViewById(R.id.iv_detail_play);
        playIv.setImageResource(R.drawable.ic_play_pause);
        movebcakIv = findViewById(R.id.iv_detail_moveback);
        moveForwardIv = findViewById(R.id.iv_detail_moveforward);
        //4. 开启服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        //7. 绑定服务
        MyConn myConn = new MyConn();
        bindService(intent, myConn, BIND_AUTO_CREATE);
        //8. 设置进度条拖动事件
        progressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iService.callSeekToPos(seekBar.getProgress());
            }
        });
        //点击事件的处理
        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (-1 == songStatus) {
                    //说明当前没有歌曲在播放
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    iService.callPlayMusic("http://music.163.com/song/media/outer/url?id=424060342.mp3");
                    Log.d("zz", "zz");
                    songStatus = 1;//设为1 表示当前正在播放
                    playIv.setImageResource(R.drawable.ic_play_running);
                } else if (1 == songStatus) {
                    iService.callPauseMusic();
                    songStatus = 0;//设为0 表示当前正在暂停
                    playIv.setImageResource(R.drawable.ic_play_pause);
                } else if (0 == songStatus) {
                    iService.callConMusic();
                    songStatus = 1;
                    playIv.setImageResource(R.drawable.ic_play_running);
                }
            }
        });
        //返回上一首歌曲
        movebcakIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = (IService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

