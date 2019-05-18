package com.example.elf.ui.activities;

import android.content.Intent;
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

import com.example.elf.R;
import com.example.elf.widget.CirCleImageView;

public class PlayActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
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
        defaultAvatarCiv.setImageResource(R.drawable.ic_default_avatar);
        //对进入歌曲详情页面的点击事件的监听
        ImageView toDetailIv = findViewById(R.id.iv_main_to_detail);
        toDetailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this, SongDetailActivity.class);
                startActivity(intent);
            }
        });

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
