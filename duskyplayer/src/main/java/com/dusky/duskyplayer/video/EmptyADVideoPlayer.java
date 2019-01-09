package com.dusky.duskyplayer.video;

import android.content.Context;
import android.util.AttributeSet;

import com.dusky.duskyplayer.R;

import tv.danmaku.ijk.media.player.IjkLibLoader;


/**
 * 空白广告视频布局
 */

public class EmptyADVideoPlayer extends ADVideoPlayer {


    public EmptyADVideoPlayer(Context context) {
        super(context);
    }

    public EmptyADVideoPlayer(Context context, IjkLibLoader ijkLibLoader) {
        super(context, ijkLibLoader);
    }

    public EmptyADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_ad;
    }

    @Override
    public void startPlayLogic() {
        prepareVideo();
        setNeedMute(true);//设置静音视频
    }


    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoResume() {

    }
}
