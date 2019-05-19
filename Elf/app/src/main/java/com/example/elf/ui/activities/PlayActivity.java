package com.example.elf.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elf.R;

import com.example.elf.songData;
import com.example.elf.utils.imageloader.loader.ImageLoader;
import com.example.elf.utils.imageloader.loader.LoaderOptions;
import com.example.elf.widget.CirCleImageView;


public class PlayActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public static final String HAPPY = "HAPPY";
    public static final String UNHAPPY = "UNHAPPY";
    public static final String CLAM = "CLAM";
    public static final String EXCITING = "EXCITING";
    public static String currentMood = HAPPY;//默认心情为高兴
    private static TextView songNameTv;
    private static TextView singerNameTv;
    private songData mSongData;


    //对menu里的东西的编写
    private MenuItem mDailyRecommend;//日推
    private MenuItem mCommentsPlaza;//广场
    private MenuItem mMyCollection;//我的收藏


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //实例化控件
        initView();


    }


    private void initView() {
        songNameTv = findViewById(R.id.tv_main_song_name);
        singerNameTv = findViewById(R.id.tv_main_singer_name);
        //一些与滑动菜单有关的设置
        mDrawerLayout = findViewById(R.id.dl_main);
        NavigationView navigationView = findViewById(R.id.nav_main);
        navigationView.setCheckedItem(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_setting);
        }
        //设置默认的头像
        View headView = navigationView.getHeaderView(0);
        CirCleImageView defaultMusicCiv = findViewById(R.id.civ_default_music);
        defaultMusicCiv.setImageResource(R.drawable.ic_default_bottom_music_icon);
        CirCleImageView defaultAvatarCiv = headView.findViewById(R.id.civ_avatar);
        defaultAvatarCiv.setImageResource(R.drawable.ic_mood_unhappy);
        //对进入歌曲详情页面的点击事件的监听
        ImageView toDetailIv = findViewById(R.id.iv_main_to_detail);

        toDetailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, SongDetailActivity.class);
                startActivity(intent);
            }
        });

        //获得MenuItem的实例
        mDailyRecommend = navigationView.getMenu().findItem(R.id.nav_recommend);
        mCommentsPlaza = navigationView.getMenu().findItem(R.id.nav_plaza);
        mMyCollection = navigationView.getMenu().findItem(R.id.nav_collection);

        //MenuItem点击事件的处理
        mDailyRecommend.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(PlayActivity.this, DailyRecommendActivity.class);
                startActivity(intent);
                return true;
            }
        });

        mCommentsPlaza.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(PlayActivity.this, CommentsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        mMyCollection.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(PlayActivity.this, CollectionActivity.class);
                startActivity(intent);
                return true;
            }
        });
        mSongData = (songData) getApplication();
        songNameTv.setText(mSongData.getSongName());
        singerNameTv.setText(mSongData.getSingerName());
        if (mSongData.getPicUrl() != null) {
            LoaderOptions request = new LoaderOptions.ImageLoaderBuilder()
                    .uri(mSongData.getPicUrl())
                    .whereToLoadFrom("ThreeCache")
                    .reqWidth(240)
                    .reqHeight(240)
                    .imageView(defaultMusicCiv)
                    .build();
            ImageLoader imageLoader = ImageLoader.getImageLoaderInstance(PlayActivity.this);
            imageLoader.bindBitmap(request);
        }
    }


    //对NavigationIcon的设置
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


}
