package com.dusky.duskyplayer.music;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.dusky.duskyplayer.listener.MediaPlayerListener;
import com.dusky.duskyplayer.major.MusicManager;
import com.dusky.duskyplayer.utils.CommonUtil;
import com.dusky.duskyplayer.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.dusky.duskyplayer.utils.CommonUtil.getTextSpeed;


public abstract class BaseMusicPlayer extends FrameLayout implements MediaPlayerListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = BaseMusicPlayer.class.getSimpleName();

    public static final int CURRENT_STATE_NORMAL = 0; //正常
    public static final int CURRENT_STATE_PREPAREING = 1; //准备中
    public static final int CURRENT_STATE_PLAYING = 2; //播放中
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3; //开始缓冲
    public static final int CURRENT_STATE_PAUSE = 5; //暂停
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6; //自动播放结束
    public static final int CURRENT_STATE_ERROR = 7; //错误状态

    protected static int mBackUpPlayingBufferState = -1;


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


    public static boolean IF_RELEASE_WHEN_ON_PAUSE = true;

    protected Timer UPDATE_PROGRESS_TIMER;


    protected ProgressTimerTask mProgressTimerTask;

    protected AudioManager mAudioManager; //音频焦点的监听

    protected Handler mHandler = new Handler();

    protected String mPlayTag = ""; //播放的tag，防止错误，因为普通的url也可能重复

    protected int mPlayPosition = -22; //播放的tag，防止错误，因为普通的url也可能重复


    protected int mBuffterPoint;//缓存进度


    protected long mSeekOnStart = -1; //从哪个开始播放

    protected long mPauseTime; //保存暂停时的时间

    protected long mCurrentPosition; //当前的播放位置

    protected boolean mTouchingProgressBar = false;

    protected ImageView mStartButton;

    protected SeekBar mProgressBar;

    protected TextView mCurrentTimeTextView, mTotalTimeTextView;


    /**
     * 当前UI
     */
    public abstract int getLayoutId();

    /**
     * 开始播放
     */
    public abstract void start();


    /**
     * 模仿IjkMediaPlayer的构造函数，提供自定义IjkLibLoader的入口
     */
    public BaseMusicPlayer(Context context, IjkLibLoader ijkLibLoader) {
        super(context);
        MusicManager.setIjkLibLoader(ijkLibLoader);
        init(context);
    }

    public BaseMusicPlayer(Context context) {
        super(context);
        init(context);
    }

    public BaseMusicPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        View.inflate(context, getLayoutId(), this);
        mAudioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

    }


    /**
     * 设置自定义so包加载类，必须在setUp之前调用
     * 不然setUp时会第一次实例化VideoManager
     */
    public void setIjkLibLoader(IjkLibLoader libLoader) {
        MusicManager.setIjkLibLoader(libLoader);
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
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects) {
        mCurrentState = CURRENT_STATE_NORMAL;
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
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects) {
        mCache = cacheWithPlay;
        mCachePath = cachePath;
        mOriginUrl = url;
        mCurrentState = CURRENT_STATE_NORMAL;
        if (cacheWithPlay && url.startsWith("http") && !url.contains("127.0.0.1")) {
            HttpProxyCacheServer proxy = MusicManager.getProxy(getContext().getApplicationContext(), cachePath);
            //此处转换了url，然后再赋值给mUrl。
            url = proxy.getProxyUrl(url);
            mCacheFile = (!url.startsWith("http"));
            //注册上缓冲监听
            if (!mCacheFile && MusicManager.instance() != null) {
                proxy.registerCacheListener(MusicManager.instance(), mOriginUrl);
            }
        } else if (!cacheWithPlay && (!url.startsWith("http") && !url.startsWith("rtmp") && !url.startsWith("rtsp"))) {
            mCacheFile = true;
        }
        this.mUrl = url;
        this.mObjects = objects;
        setState(CURRENT_STATE_NORMAL);
        return true;
    }

    /**
     * 设置播放状态
     *
     * @param state
     */
    protected void setState(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    cancelProgressTimer();
                    MusicManager.instance().releaseMediaPlayer();
                    mBuffterPoint = 0;
                }
                if (mAudioManager != null) {
                    mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
                break;
            case CURRENT_STATE_PREPAREING:
                resetProgressAndTime(false);
                break;
            case CURRENT_STATE_PLAYING:
                startProgressTimer();
                break;
            case CURRENT_STATE_PAUSE:
                startProgressTimer();
                break;
            case CURRENT_STATE_ERROR:
                if (isCurrentMediaListener()) {
                    MusicManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                cancelProgressTimer();
                mProgressBar.setProgress(100);
                mCurrentTimeTextView.setText(mTotalTimeTextView.getText());
                break;
        }
    }


    public void onClickStart(View v) {
        if (mStartButton!=null&&v==mStartButton) {
            if (TextUtils.isEmpty(mUrl)) {
                Toast.makeText(getContext(),"播放地址为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(mCurrentState == CURRENT_STATE_NORMAL){
                startButtonLogic();
            }
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                MusicManager.instance().getMediaPlayer().pause();
                setState(CURRENT_STATE_PAUSE);

            } else if (mCurrentState == CURRENT_STATE_PAUSE) {
                MusicManager.instance().getMediaPlayer().start();
                setState(CURRENT_STATE_PLAYING);
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                startButtonLogic();
            }
        } else if (mCurrentState == CURRENT_STATE_ERROR) {
            prepareVideo();
        }
    }


    /**
     * 播放按键的逻辑
     */
    private void startButtonLogic() {
        prepareVideo();
    }

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        if (MusicManager.instance().listener() != null) {
            MusicManager.instance().listener().onCompletion();
        }
        MusicManager.instance().setListener(this);
        MusicManager.instance().setPlayTag(mPlayTag);
        MusicManager.instance().setPlayPosition(mPlayPosition);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBackUpPlayingBufferState = -1;
        MusicManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
        setState(CURRENT_STATE_PREPAREING);
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
                            releaseAllMedia();
                        }
                    });
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (MusicManager.instance().getMediaPlayer().isPlaying()) {
                        MusicManager.instance().getMediaPlayer().pause();
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
    public void onReset() {
        setState(CURRENT_STATE_NORMAL);
    }

    /**
     * 暂停状态
     */
    @Override
    public void onPause() {
        if (MusicManager.instance().getMediaPlayer().isPlaying()) {
            setState(CURRENT_STATE_PAUSE);
            mPauseTime = System.currentTimeMillis();
            mCurrentPosition = MusicManager.instance().getMediaPlayer().getCurrentPosition();
            if (MusicManager.instance().getMediaPlayer() != null)
                MusicManager.instance().getMediaPlayer().pause();
        }
    }

    /**
     * 恢复暂停状态
     */
    @Override
    public void onResume() {
        mPauseTime = 0;
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mCurrentPosition > 0 && MusicManager.instance().getMediaPlayer() != null) {
                setState(CURRENT_STATE_PLAYING);
                MusicManager.instance().getMediaPlayer().seekTo(mCurrentPosition);
                MusicManager.instance().getMediaPlayer().start();
            }
        }
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }


    /***
     * 拖动进度条
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (MusicManager.instance().getMediaPlayer() != null && mHadPlay) {
            try {
                int time = seekBar.getProgress() * getDuration() / 100;
                MusicManager.instance().getMediaPlayer().seekTo(time);
            } catch (Exception e) {
                Log.d(TAG, "onStopTrackingTouch: "+e.toString());
            }
        }
    }

    @Override
    public void onPrepared() {
        if (mCurrentState != CURRENT_STATE_PREPAREING) return;

        if (MusicManager.instance().getMediaPlayer() != null) {
            MusicManager.instance().getMediaPlayer().start();
        }

        startProgressTimer();

        setState(CURRENT_STATE_PLAYING);

        if (MusicManager.instance().getMediaPlayer() != null && mSeekOnStart > 0) {
            MusicManager.instance().getMediaPlayer().seekTo(mSeekOnStart);
            mSeekOnStart = 0;
        }

        mHadPlay = true;
    }

    @Override
    public void onAutoCompletion() {
        setState(CURRENT_STATE_AUTO_COMPLETE);

        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setState(CURRENT_STATE_NORMAL);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != CURRENT_STATE_NORMAL && mCurrentState != CURRENT_STATE_PREPAREING) {
            if (percent != 0) {
                setTextAndProgress(percent);
                mBuffterPoint = percent;
            }
            //循环清除进度
            if (mLooping && mHadPlay && percent == 0 && mProgressBar.getProgress() >= (mProgressBar.getMax() - 1)) {
                resetProgressAndTime(true);
            }
        }
    }

    @Override
    public void onError(int what, int extra) {
        if (what != 38 && what != -38) {
            setState(CURRENT_STATE_ERROR);
            deleteCacheFileWhenError();
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mBackUpPlayingBufferState = mCurrentState;
            //避免在onPrepared之前就进入了buffering，导致一只loading
            if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                setState(CURRENT_STATE_PLAYING_BUFFERING_START);

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (mBackUpPlayingBufferState != -1) {

                if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                    setState(mBackUpPlayingBufferState);

                mBackUpPlayingBufferState = -1;
            }
        }
    }



    public void setSpeed(float speed) {
        this.mSpeed = speed;
        if (MusicManager.instance().getMediaPlayer() != null
                && MusicManager.instance().getMediaPlayer() instanceof IjkMediaPlayer) {
            if (speed != 1 && speed > 0) {
                ((IjkMediaPlayer) MusicManager.instance().getMediaPlayer()).setSpeed(speed);
            }
        }
    }

    public float getSpeed() {
        return mSpeed;
    }

    /**
     * 设置循环
     */
    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }

    public boolean isLooping() {
        return mLooping;
    }




    /**
     * 清除当前缓存
     */
    public void clearCurrentCache() {
        if (mCacheFile) {
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

    protected void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }

    }

    protected class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextAndProgress(0);
                    }
                });
            }
        }
    }

    /**
     * 获取当前播放进度
     */
    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                position = (int) MusicManager.instance().getMediaPlayer().getCurrentPosition();
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
            duration = (int) MusicManager.instance().getMediaPlayer().getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) mProgressBar.setProgress(progress);
        }
        if (secProgress > 94) secProgress = 100;
        if (secProgress != 0 && !mCacheFile) {
            mProgressBar.setSecondaryProgress(secProgress);
        }
        mTotalTimeTextView.setText(CommonUtil.stringForTime(totalTime));
        if (currentTime > 0)
            mCurrentTimeTextView.setText(CommonUtil.stringForTime(currentTime));
    }


    /**
     * 重置时间和进度条UI
     * @param loop
     */
    protected void resetProgressAndTime(boolean loop) {
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);
        mCurrentTimeTextView.setText(CommonUtil.stringForTime(0));
        if(!loop){
            mTotalTimeTextView.setText(CommonUtil.stringForTime(0));
        }
    }

    /**
     * 页面销毁了记得调用
     */
    public static void releaseAllMedia() {
        if (IF_RELEASE_WHEN_ON_PAUSE) {
            if (MusicManager.instance().listener() != null) {
                MusicManager.instance().listener().onCompletion();
            }
            MusicManager.instance().releaseMediaPlayer();
        } else {
            IF_RELEASE_WHEN_ON_PAUSE = true;
        }
    }

    /**
     * 释放掉
     */
    public void release() {
        releaseAllMedia();
        mHadPlay = false;
    }


    protected boolean isCurrentMediaListener() {
        return MusicManager.instance().listener() != null
                && MusicManager.instance().listener() == this;
    }


    /**
     * 获取播放按键
     */
    public View getStartButton() {
        return mStartButton;
    }
    public void setmStartButton(ImageView mStartButton) {
        this.mStartButton = mStartButton;
    }

    /**
     * 获取当前播放状态
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     */
    public String getPlayTag() {
        return mPlayTag;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     *
     * @param playTag 保证不重复就好
     */
    public void setPlayTag(String playTag) {
        this.mPlayTag = playTag;
    }


    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public long getNetSpeed() {
        if (MusicManager.instance().getMediaPlayer() != null
                && (MusicManager.instance().getMediaPlayer() instanceof IjkMediaPlayer)) {
            return ((IjkMediaPlayer) MusicManager.instance().getMediaPlayer()).getTcpSpeed();
        } else {
            return -1;
        }

    }

    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public String getNetSpeedText() {
        long speed = getNetSpeed();
        return getTextSpeed(speed);
    }

    public long getSeekOnStart() {
        return mSeekOnStart;
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