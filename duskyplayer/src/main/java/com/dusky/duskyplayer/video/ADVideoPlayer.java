package com.dusky.duskyplayer.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.dusky.duskyplayer.R;
import com.dusky.duskyplayer.major.VideoManager;
import com.dusky.duskyplayer.utils.CommonUtil;
import com.dusky.duskyplayer.utils.StorageUtils;

import java.io.File;
import java.util.Map;
import java.util.Timer;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkLibLoader;


public abstract class ADVideoPlayer extends BaseADVideoPlayer implements TextureView.SurfaceTextureListener {

    public static final String TAG = "VideoPlayer";


    public static final int CURRENT_STATE_NORMAL = 0; //正常
    public static final int CURRENT_STATE_PREPAREING = 1; //准备中
    public static final int CURRENT_STATE_PLAYING = 2; //播放中
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3; //开始缓冲
    public static final int CURRENT_STATE_PAUSE = 5; //暂停
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6; //自动播放结束
    public static final int CURRENT_STATE_ERROR = 7; //错误状态

    public static final int FULL_SCREEN_NORMAL_DELAY = 2000;

    protected static int mBackUpPlayingBufferState = -1;

    protected static boolean IF_FULLSCREEN_FROM_NORMAL = false;

    public static boolean IF_RELEASE_WHEN_ON_PAUSE = true;

    protected Timer UPDATE_PROGRESS_TIMER;

    protected Surface mSurface;

    protected AudioManager mAudioManager; //音频焦点的监听

    protected Handler mHandler = new Handler();

    protected String mPlayTag = ""; //播放的tag，防止错误，因为普通的url也可能重复

    protected Matrix mTransformCover = null;

    protected int mPlayPosition = -22; //播放的tag，防止错误，因为普通的url也可能重复

    protected float mDownX;//触摸的X

    protected float mDownY; //触摸的Y

    protected float mMoveY;

    protected float mBrightnessData = -1; //亮度

    protected int mDownPosition; //手指放下的位置

    protected int mGestureDownVolume; //手势调节音量的大小

    protected int mScreenWidth; //屏幕宽度

    protected int mScreenHeight; //屏幕高度

    protected int mThreshold = 80; //手势偏差值

    protected int mSeekToInAdvance = -1;

    protected int mBuffterPoint;//缓存进度

    protected int mSeekTimePosition; //手动改变滑动的位置

    protected int mSeekEndOffset; //手动滑动的起始偏移位置

    protected long mSeekOnStart = -1; //从哪个开始播放

    protected long mPauseTime; //保存暂停时的时间

    protected long mCurrentPosition; //当前的播放位置

    protected boolean mTouchingProgressBar = false;

    protected boolean mChangeVolume = false;//是否改变音量

    protected boolean mChangePosition = false;//是否改变播放进度

    protected boolean mShowVKey = false; //触摸显示虚拟按键

    protected boolean mBrightness = false;//是否改变亮度

    protected boolean mFirstTouch = false;//是否首次触摸


    /**
     * 当前UI
     */
    public abstract int getLayoutId();

    /**
     * 开始播放
     */
    public abstract void startPlayLogic();

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public ADVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    /**
     * 模仿IjkMediaPlayer的构造函数，提供自定义IjkLibLoader的入口
     */
    public ADVideoPlayer(Context context, IjkLibLoader ijkLibLoader) {
        super(context);
        VideoManager.setIjkLibLoader(ijkLibLoader);
        init(context);
    }

    public ADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        View.inflate(context, getLayoutId(), this);

        mTextureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }


    /**
     * 设置自定义so包加载类，必须在setUp之前调用
     * 不然setUp时会第一次实例化GSYVideoManager
     */
    public void setIjkLibLoader(IjkLibLoader libLoader) {
        VideoManager.setIjkLibLoader(libLoader);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    public boolean setUp(String url, boolean cacheWithPlay, Object... objects) {
        return setUp(url, cacheWithPlay, ((File) null), objects);
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param mapHeadData   头部信息
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects) {
        if (setUp(url, cacheWithPlay, cachePath, objects)) {
            this.mMapHeadData.clear();
            if (mapHeadData != null)
                this.mMapHeadData.putAll(mapHeadData);
            return true;
        }
        return false;
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects) {
        mCache = cacheWithPlay;
        mCachePath = cachePath;
        mOriginUrl = url;
        mCurrentState = CURRENT_STATE_NORMAL;
        if (cacheWithPlay && url.startsWith("http") && !url.contains("127.0.0.1")) {
            HttpProxyCacheServer proxy = VideoManager.getProxy(getContext().getApplicationContext(), cachePath);
            //此处转换了url，然后再赋值给mUrl。
            url = proxy.getProxyUrl(url);
            mCacheFile = (!url.startsWith("http"));
            //注册上缓冲监听
            if (!mCacheFile && VideoManager.instance() != null) {
                proxy.registerCacheListener(VideoManager.instance(), mOriginUrl);
            }
        } else if (!cacheWithPlay && (!url.startsWith("http") && !url.startsWith("rtmp") && !url.startsWith("rtsp"))) {
            mCacheFile = true;
        }
        this.mUrl = url;
        this.mObjects = objects;
        setStateAndUi(CURRENT_STATE_NORMAL);
        return true;
    }

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected void setStateAndUi(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    VideoManager.instance().releaseMediaPlayer();
                    mBuffterPoint = 0;
                }
                if (mAudioManager != null) {
                    mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
                break;
            case CURRENT_STATE_PREPAREING:

                break;
            case CURRENT_STATE_PLAYING:

                break;
            case CURRENT_STATE_PAUSE:

                break;
            case CURRENT_STATE_ERROR:
                if (isCurrentMediaListener()) {
                    VideoManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:

                break;
        }
    }

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onCompletion();
        }
        VideoManager.instance().setListener(this);
        VideoManager.instance().setPlayTag(mPlayTag);
        VideoManager.instance().setPlayPosition(mPlayPosition);
        addTextureView();
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBackUpPlayingBufferState = -1;
        VideoManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
        setStateAndUi(CURRENT_STATE_PREPAREING);
    }

    /**
     * 监听是否有外部其他多媒体开始播放
     */
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            releaseAllVideos();
                        }
                    });
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (VideoManager.instance().getMediaPlayer().isPlaying()) {
                        VideoManager.instance().getMediaPlayer().pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };


    /**
     * 重置
     */
    public void onVideoReset() {
        setStateAndUi(CURRENT_STATE_NORMAL);
    }


    /**
     * 添加播放的view
     */
    protected void addTextureView() {
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }
        mMyTextureView = null;
        mMyTextureView = new MyTextureView(getContext());
        mMyTextureView.setSurfaceTextureListener(this);

        if(mTextureViewContainer instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureViewContainer.addView(mMyTextureView, layoutParams);
        } else if(mTextureViewContainer instanceof FrameLayout) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            mTextureViewContainer.addView(mMyTextureView, layoutParams);
        }
    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        VideoManager.instance().setDisplay(mSurface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        VideoManager.instance().setDisplay(null);
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        //如果播放的是暂停全屏了
    }





    @Override
    public void onPrepared() {
        if (mCurrentState != CURRENT_STATE_PREPAREING) return;

        if (VideoManager.instance().getMediaPlayer() != null) {
            VideoManager.instance().getMediaPlayer().start();
        }

        if (VideoManager.instance().getMediaPlayer() != null && mSeekToInAdvance != -1) {
            VideoManager.instance().getMediaPlayer().seekTo(mSeekToInAdvance);
            mSeekToInAdvance = -1;
        }

        setStateAndUi(CURRENT_STATE_PLAYING);

        if (mVideoAllCallBack != null && isCurrentMediaListener()) {

            mVideoAllCallBack.onPrepared(mUrl, mObjects);
        }

        if (VideoManager.instance().getMediaPlayer() != null && mSeekOnStart > 0) {
            VideoManager.instance().getMediaPlayer().seekTo(mSeekOnStart);
            mSeekOnStart = 0;
        }
    }

    @Override
    public void onAutoCompletion() {
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {

            mVideoAllCallBack.onAutoComplete(mUrl, mObjects);
        }
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (IF_FULLSCREEN_FROM_NORMAL) {
            IF_FULLSCREEN_FROM_NORMAL = false;
            if (VideoManager.instance().lastListener() != null) {
                VideoManager.instance().lastListener().onAutoCompletion();
            }
        }
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL);
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            IF_FULLSCREEN_FROM_NORMAL = false;
            if (VideoManager.instance().lastListener() != null) {
                VideoManager.instance().lastListener().onCompletion();//回到上面的onAutoCompletion
            }
        }
        VideoManager.instance().setCurrentVideoHeight(0);
        VideoManager.instance().setCurrentVideoWidth(0);

        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != CURRENT_STATE_NORMAL && mCurrentState != CURRENT_STATE_PREPAREING) {
            if (percent != 0) {
                mBuffterPoint = percent;

            }
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {
        if (what != 38 && what != -38) {
            setStateAndUi(CURRENT_STATE_ERROR);
            deleteCacheFileWhenError();
            if (mVideoAllCallBack != null) {
                mVideoAllCallBack.onPlayError(mUrl, mObjects);
            }
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mBackUpPlayingBufferState = mCurrentState;

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (mBackUpPlayingBufferState != -1) {
                mBackUpPlayingBufferState = -1;
            }
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {

        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = VideoManager.instance().getCurrentVideoWidth();
        int mVideoHeight = VideoManager.instance().getCurrentVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mMyTextureView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {

    }

    /**
     * 清除当前缓存
     */
    public void clearCurrentCache() {
        if (mCacheFile) {
            //是否为缓存文件

            //可能是因为缓存文件除了问题
            CommonUtil.deleteFile(mUrl.replace("file://", ""));
            mUrl = mOriginUrl;
        } else if (mUrl.contains("127.0.0.1")) {
            //是否为缓存了未完成的文件
            Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
            String name = md5FileNameGenerator.generate(mOriginUrl);
            if (mCachePath != null) {
                String path = mCachePath.getAbsolutePath() + File.separator + name + ".download";
                CommonUtil.deleteFile(path);
            } else {
                String path = StorageUtils.getIndividualCacheDirectory
                        (getContext().getApplicationContext()).getAbsolutePath()
                        + File.separator + name + ".download";
                CommonUtil.deleteFile(path);
            }
        }

    }


    /**
     * 播放错误的时候，删除缓存文件
     */
    private void deleteCacheFileWhenError() {
        clearCurrentCache();
        mUrl = mOriginUrl;
    }


    /**
     * 获取当前播放进度
     */
    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                position = (int) VideoManager.instance().getMediaPlayer().getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    /**
     * 获取当前总时长
     */
    public int getDuration() {
        int duration = 0;
        try {
            duration = (int) VideoManager.instance().getMediaPlayer().getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }


    /**
     * 页面销毁了记得调用销毁video
     */
    public static void releaseAllVideos() {
        if (IF_RELEASE_WHEN_ON_PAUSE) {
            if (VideoManager.instance().listener() != null) {
                VideoManager.instance().listener().onCompletion();
            }
            VideoManager.instance().releaseMediaPlayer();
        } else {
            IF_RELEASE_WHEN_ON_PAUSE = true;
        }
    }

    /**
     * 是否需要静音
     */
    public void setNeedMute(boolean needMute) {
        VideoManager.instance().setNeedMute(needMute);
    }


    protected boolean isCurrentMediaListener() {
        return VideoManager.instance().listener() != null
                && VideoManager.instance().listener() == this;
    }



    /**
     * 从哪里开始播放
     * 目前有时候前几秒有跳动问题，毫秒
     * 需要在startPlayLogic之前，即播放开始之前
     */
    public void setSeekOnStart(long seekOnStart) {
        this.mSeekOnStart = seekOnStart;
    }


    /**
     * 缓冲进度/缓存进度
     */
    public int getBuffterPoint() {
        return mBuffterPoint;
    }

}