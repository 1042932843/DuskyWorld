package com.dusky.world.Module.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dusky.duskyplayer.video.ADVideoPlayer;
import com.dusky.duskyplayer.video.EmptyADVideoPlayer;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class SplashActivity extends BaseActivity {
    int time=5000;

    @BindView(R.id.splash)
    ImageView splash;

    @BindView(R.id.jump)
    TextView jump;
    @OnClick(R.id.jump)
    public void jump(){
        startActivity(new Intent(this,HomePage.class));
        ADVideoPlayer.releaseAllVideos();
        finish();
    }

    @BindView(R.id.video_player)
    EmptyADVideoPlayer video_player;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init(Bundle savedInstanceState) {
        String source1 = "https://raw.githubusercontent.com/1042932843/img-folder/master/adtest.mp4";

        //source1="file://"+"/storage/emulated/0/Movies/Screenrecords/S81122-16271597.mp4";
        //增加title
        if(!TextUtils.isEmpty(source1)){
            video_player.setUp(source1,true,"");
            video_player.startPlayLogic();
        }else{
            Glide.with(this).load(R.drawable.banner).into(splash);
        }

        Observable.timer(time, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    startActivity(new Intent(this,HomePage.class));
                    finish();
                });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        video_player=null;
        ADVideoPlayer.releaseAllVideos();
    }
}
