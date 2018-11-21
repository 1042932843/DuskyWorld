package com.dusky.world.Module.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dusky.duskyplayer.video.ADVideoPlayer;
import com.dusky.duskyplayer.video.StandardADVideoPlayer;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class Splash extends BaseActivity {

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
    StandardADVideoPlayer video_player;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void init(Bundle savedInstanceState) {
        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        //增加title
        if(!TextUtils.isEmpty(source1)){
            video_player.setUp(source1,true,"");
            video_player.startPlayLogic();
            int time=video_player.getDuration();
        }else{
            Glide.with(this).load(R.drawable.banner).into(splash);
        }

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    startActivity(new Intent(this,HomePage.class));
                    ADVideoPlayer.releaseAllVideos();
                    finish();
                });
    }


}
