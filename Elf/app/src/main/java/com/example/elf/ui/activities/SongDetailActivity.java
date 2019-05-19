package com.example.elf.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.elf.R;
import com.example.elf.bean.SongDetailBean;
import com.example.elf.service.IService;
import com.example.elf.service.MusicService;
import com.example.elf.songData;
import com.example.elf.utils.imageloader.loader.ImageLoader;
import com.example.elf.utils.imageloader.loader.LoaderOptions;
import com.example.elf.utils.netrequsethelper.NetRequestHelper;
import com.example.elf.utils.netrequsethelper.NetRequestOptions;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.elf.config.Api.getSongFilePr;
import static com.example.elf.config.Api.getSongFileSu;
import static com.example.elf.config.Api.getSongListDetail;
import static com.example.elf.config.Api.getSongListID;
import static com.example.elf.ui.activities.PlayActivity.currentMood;


public class SongDetailActivity extends AppCompatActivity {
    static NetRequestHelper netRequestHelper = NetRequestHelper.obtainNetRequestHelper();
    public int songStatus;
    public IService iService;
    private static SeekBar progressSb;
    private ImageView playIv;
    private ImageView movebcakIv;
    private ImageView moveForwardIv;
    private static TextView currentTimeTv;
    private static TextView allTimeTv;
    private static int songPosition = 0;//当前歌曲在集合中所在的位置  默认第零首
    private static List<SongDetailBean> mList;
    private static TextView songNameTv;
    private static TextView singerNameTv;
    private ImageView picIv;
    private ImageView backIv;
    private ImageView downloadIv;
    private songData mMyAppData;


    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");
            progressSb.setMax(duration);
            progressSb.setProgress(currentPosition);
            currentTimeTv.setText(parseTime(currentPosition));
            allTimeTv.setText(parseTime(duration));
        }
    };

    public Handler detailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mList = (List<SongDetailBean>) msg.obj;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        mMyAppData = (songData) getApplication();
        getSongList();
        //找到控件
        songNameTv = findViewById(R.id.tv_detail_song_name);
        singerNameTv = findViewById(R.id.tv_detail_singer_name);
        picIv = findViewById(R.id.iv_detail_pic);
        progressSb = findViewById(R.id.sb_detail);
        playIv = findViewById(R.id.iv_detail_play);
        if (1 == mMyAppData.getSongStatus()) {
            playIv.setImageResource(R.drawable.ic_play_running);
        } else {
            playIv.setImageResource(R.drawable.ic_play_pause);
        }
        movebcakIv = findViewById(R.id.iv_detail_moveback);
        moveForwardIv = findViewById(R.id.iv_detail_moveforward);
        currentTimeTv = findViewById(R.id.tv_detail_current_time);
        allTimeTv = findViewById(R.id.tv_detail_all_time);
        backIv = findViewById(R.id.iv_detail_back);
        downloadIv = findViewById(R.id.iv_detail_download);
        if (mMyAppData.getSingerName() != null & mMyAppData.getSingerName() != null) {
            singerNameTv.setText(mMyAppData.getSingerName());
            songNameTv.setText(mMyAppData.getSongName());
        }
        if (mMyAppData.getPicUrl() != null) {
            LoaderOptions request = new LoaderOptions.ImageLoaderBuilder()
                    .uri(mMyAppData.getPicUrl())
                    .whereToLoadFrom("ThreeCache")
                    .reqWidth(400)
                    .reqHeight(250)
                    .imageView(picIv)
                    .build();
            ImageLoader imageLoader = ImageLoader.getImageLoaderInstance(SongDetailActivity.this);
            imageLoader.bindBitmap(request);
        }
        // 开启服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        // 绑定服务
        MyConn myConn = new MyConn();
        bindService(intent, myConn, BIND_AUTO_CREATE);
        // 设置进度条拖动事件
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
//                if (mList == null) {
//                    songPosition = new Random().nextInt(mList.size());
//                }
                if (songStatus != 0 & songStatus != 1) {
                    songStatus = -1;
                } else {
                    songStatus = mMyAppData.getSongStatus();
                }

                if (-1 == songStatus) {
                    //说明当前没有歌曲在播放
                    iService.callPlayMusic(getSongFilePr + mList.get(songPosition).getSongId() + getSongFileSu);
                    mMyAppData.setSongName(mList.get(songPosition).getSingerName());
                    mMyAppData.setSingerName(mList.get(songPosition).getSongName());
                    mMyAppData.setPicUrl(mList.get(songPosition).getPicUrl());
                    mMyAppData.setSongStatus(1);
                    singerNameTv.setText(mMyAppData.getSingerName());
                    songNameTv.setText(mMyAppData.getSongName());
                    LoaderOptions request = new LoaderOptions.ImageLoaderBuilder()
                            .uri(mMyAppData.getPicUrl())
                            .whereToLoadFrom("ThreeCache")
                            .reqWidth(400)
                            .reqHeight(250)
                            .imageView(picIv)
                            .build();
                    ImageLoader imageLoader = ImageLoader.getImageLoaderInstance(SongDetailActivity.this);
                    imageLoader.bindBitmap(request);


                    playIv.setImageResource(R.drawable.ic_play_running);
                } else if (1 == songStatus) {
                    iService.callPauseMusic();
                    mMyAppData.setSongStatus(0);//设为0 表示当前正在暂停
                    playIv.setImageResource(R.drawable.ic_play_pause);
                } else if (0 == songStatus) {
                    iService.callConMusic();
                    mMyAppData.setSongStatus(1);
                    playIv.setImageResource(R.drawable.ic_play_running);
                }
            }
        });
        //返回上一首歌曲
        movebcakIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == songPosition) {
                    songPosition = mList.size() - 1;
                } else {
                    --songPosition;
                }
                iService.callPlayMusic(getSongFilePr + mList.get(songPosition).getSongId() + getSongFileSu);
                mMyAppData.setSongName(mList.get(songPosition).getSingerName());
                mMyAppData.setSingerName(mList.get(songPosition).getSongName());
                mMyAppData.setPicUrl(mList.get(songPosition).getPicUrl());
                mMyAppData.setSongStatus(1);
                singerNameTv.setText(mMyAppData.getSingerName());
                songNameTv.setText(mMyAppData.getSongName());
                LoaderOptions request = new LoaderOptions.ImageLoaderBuilder()
                        .uri(mMyAppData.getPicUrl())
                        .whereToLoadFrom("ThreeCache")
                        .reqWidth(400)
                        .reqHeight(250)
                        .imageView(picIv)
                        .build();
                ImageLoader imageLoader = ImageLoader.getImageLoaderInstance(SongDetailActivity.this);
                imageLoader.bindBitmap(request);

                songStatus = mMyAppData.getSongStatus();//设为1 表示当前正在播放
                playIv.setImageResource(R.drawable.ic_play_running);
            }
        });
        //进入下一首歌曲
        moveForwardIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() - 1 == songPosition) {
                    songPosition = 0;
                } else {
                    ++songPosition;
                }
                iService.callPlayMusic(getSongFilePr + mList.get(songPosition).getSongId() + getSongFileSu);
                mMyAppData.setSongName(mList.get(songPosition).getSingerName());
                mMyAppData.setSingerName(mList.get(songPosition).getSongName());
                mMyAppData.setPicUrl(mList.get(songPosition).getPicUrl());
                mMyAppData.setSongStatus(1);
                singerNameTv.setText(mMyAppData.getSingerName());
                songNameTv.setText(mMyAppData.getSongName());
                LoaderOptions request = new LoaderOptions.ImageLoaderBuilder()
                        .uri(mMyAppData.getPicUrl())
                        .whereToLoadFrom("ThreeCache")
                        .reqWidth(400)
                        .reqHeight(250)
                        .imageView(picIv)
                        .build();
                ImageLoader imageLoader = ImageLoader.getImageLoaderInstance(SongDetailActivity.this);
                imageLoader.bindBitmap(request);

                songStatus = mMyAppData.getSongStatus();//设为1 表示当前正在播放
                playIv.setImageResource(R.drawable.ic_play_running);
            }
        });
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SongDetailActivity.this, PlayActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        downloadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载
            }
        });
    }


    /**
     * 时间格式化
     */
    private static String parseTime(int oldTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");// 时间格式
        String newTime = sdf.format(new Date(oldTime));
        return newTime;
    }

    private void getSongList() {
        String getSongListIDUrl = getSongListID + currentMood;
        NetRequestOptions netWorkOption = new NetRequestOptions.Builder()
                .url(getSongListIDUrl)
                .method("GET")
                .build();
        netRequestHelper.excute(netWorkOption, new NetRequestHelper.Callback() {
            @Override
            public void onSucceeded(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    String id = data.getString("id");
                    //根据id获取歌单信息
                    String getSongListDetailUrl = getSongListDetail + id;
                    NetRequestOptions netWorkOption = new NetRequestOptions.Builder()
                            .url(getSongListDetailUrl)
                            .method("GET")
                            .build();
                    netRequestHelper.excute(netWorkOption, new NetRequestHelper.Callback() {
                        @Override
                        public void onSucceeded(String response) {
                            Log.d("NetRequestHelper Test", "请求成功后返回的消息是" + response);
                            try {
                                JSONObject object1 = new JSONObject(response);
                                JSONObject playlist = object1.getJSONObject("playlist");
                                JSONArray tracks = playlist.getJSONArray("tracks");
                                List<SongDetailBean> songDetailList = new ArrayList<>();
                                for (int i = 0; i < tracks.length(); i++) {
                                    JSONObject objectTemp = (JSONObject) tracks.get(i);
                                    String songName = objectTemp.getString("name");//歌曲作者
                                    String songID = objectTemp.getString("id");//歌曲id
                                    JSONObject ar = (JSONObject) objectTemp.getJSONArray("ar").get(0);
                                    String singerName = ar.getString("name");//歌手名称
                                    JSONObject al = objectTemp.getJSONObject("al");
                                    String alName = al.getString("name");//专辑名称
                                    String picUrl = al.getString("picUrl");//要拿出来显示的图片
//                                    Log.d("zzs", "cc" + songName + songID + singerName + alName + picUrl);
                                    SongDetailBean songDetail = new SongDetailBean();
                                    songDetail.setSingerName(singerName);
                                    songDetail.setPicUrl(picUrl);
                                    songDetail.setSongId(Integer.parseInt(songID));
                                    songDetail.setSongName(songName);
                                    songDetailList.add(songDetail);
                                }
                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = songDetailList;
                                detailHandler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailed(String response) {
                            Log.d("NetRequestHelper Test", "请求失败后返回的消息是" + response);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String response) {
                Log.d("NetRequestHelper Test", "请求失败后返回的消息是" + response);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

