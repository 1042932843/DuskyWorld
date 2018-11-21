package com.dusky.duskyplayer.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dusky.duskyplayer.R;
import com.dusky.duskyplayer.listener.VideoAllCallBack;
import com.dusky.duskyplayer.listener.VideoPlayerListener;
import com.dusky.duskyplayer.major.VideoManager;
import com.dusky.duskyplayer.utils.CommonUtil;
import com.dusky.duskyplayer.utils.OrientationUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.dusky.duskyplayer.utils.CommonUtil.getActionBarHeight;
import static com.dusky.duskyplayer.utils.CommonUtil.getStatusBarHeight;
import static com.dusky.duskyplayer.utils.CommonUtil.hideNavKey;
import static com.dusky.duskyplayer.utils.CommonUtil.hideSupportActionBar;
import static com.dusky.duskyplayer.utils.CommonUtil.showNavKey;
import static com.dusky.duskyplayer.utils.CommonUtil.showSupportActionBar;


public abstract class BaseADVideoPlayer extends FrameLayout implements VideoPlayerListener {


    protected boolean mCache = false;//是否播边边缓冲

    protected boolean mNeedShowWifiTip = false; //是否需要显示流量提示


    protected int mCurrentState = -1; //当前的播放状态


    protected float mSpeed = 1;//播放速度，只支持6.0以上

    protected boolean mRotateViewAuto = true; //是否自动旋转


    protected boolean mLooping = false;//循环

    protected boolean mCacheFile = false; //是否是缓存的文件


    protected Context mContext;

    protected String mOriginUrl; //原来的url

    protected String mUrl; //转化后的URL

    protected Object[] mObjects;

    protected File mCachePath;

    protected ViewGroup mTextureViewContainer; //渲染控件父类

    protected VideoAllCallBack mVideoAllCallBack;

    protected Map<String, String> mMapHeadData = new HashMap<>();

    protected MyTextureView mMyTextureView;


    protected OrientationUtils mOrientationUtils; //旋转工具类

    private Handler mHandler = new Handler();


    public BaseADVideoPlayer(Context context) {
        super(context);
    }

    public BaseADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseADVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private ViewGroup getViewGroup() {
        return (ViewGroup) (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 移除没用的
     */
    private void removeVideo(ViewGroup vp, int id) {
        View old = vp.findViewById(id);
        if (old != null) {
            if (old.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) old.getParent();
                vp.removeView(viewGroup);
            }
        }
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
     * 添加播放的view
     */
    protected abstract void addTextureView();



    public boolean isLooping() {
        return mLooping;
    }

    /**
     * 设置循环
     */
    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }


    public boolean isRotateViewAuto() {
        return mRotateViewAuto;
    }

    /**
     * 是否开启自动旋转
     */
    public void setRotateViewAuto(boolean rotateViewAuto) {
        this.mRotateViewAuto = rotateViewAuto;
        if (mOrientationUtils != null) {
            mOrientationUtils.setEnable(rotateViewAuto);
        }
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
