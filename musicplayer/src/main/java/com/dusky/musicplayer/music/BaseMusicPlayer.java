package com.dusky.musicplayer.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;


import com.dusky.musicplayer.Listener.MediaPlayerListener;
import com.dusky.musicplayer.major.VideoManager;
import com.dusky.musicplayer.utils.CommonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public abstract class BaseMusicPlayer extends FrameLayout implements MediaPlayerListener {


    protected boolean mCache = false;//是否播边边缓冲


    protected int mCurrentState = -1; //当前的播放状态

    protected float mSpeed = 1;//播放速度，只支持6.0以上


    protected boolean mLooping = false;//循环

    protected boolean mHadPlay = false;//是否播放过

    protected boolean mCacheFile = false; //是否是缓存的文件


    protected Context mContext;

    protected String mOriginUrl; //原来的url

    protected String mUrl; //转化后的URL

    protected Object[] mObjects;

    protected File mCachePath;



    protected Map<String, String> mMapHeadData = new HashMap<>();




    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public BaseMusicPlayer(Context context, Boolean fullFlag) {
        super(context);

    }

    public BaseMusicPlayer(Context context) {
        super(context);
    }

    public BaseMusicPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseMusicPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private ViewGroup getViewGroup() {
        return (ViewGroup) (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
    }


    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects
     * @return
     */
    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects);

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param mapHeadData
     * @param objects
     * @return
     */

    public abstract boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects);

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected abstract void setStateAndUi(int state);


    protected abstract void onClickUiToggle();



    public boolean isLooping() {
        return mLooping;
    }

    /**
     * 设置循环
     */
    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }


    public float getSpeed() {
        return mSpeed;
    }

    /**
     * 播放速度
     */
    public void setSpeed(float speed) {
        this.mSpeed = speed;
        if (VideoManager.instance().getMediaPlayer() != null
                && VideoManager.instance().getMediaPlayer() instanceof IjkMediaPlayer) {
            if (speed != 1 && speed > 0) {
                ((IjkMediaPlayer) VideoManager.instance().getMediaPlayer()).setSpeed(speed);
            }
        }
    }

}
