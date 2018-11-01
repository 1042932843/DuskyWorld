package com.nbsix.player.music;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsix.player.R;

import java.io.File;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/10/16
 * @DESCRIPTION:
 */
public class StandardMusicPlayer extends MusicPlayer {
    ImageView player_pre,player_next;
    Context context;

    protected boolean mCacheFile = false; //是否是缓存的文件

    /**
     * 如果需要不同布局区分功能，需要重载
     */

    public StandardMusicPlayer(Context context) {
        super(context);
    }

    public StandardMusicPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.music_layout_standard;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        this.context=context;
        mStartButton=(ImageView) findViewById(R.id.player_start);
        mStartButton.setOnClickListener(this);
        player_pre=(ImageView) findViewById(R.id.player_pre);
        player_next=(ImageView) findViewById(R.id.player_next);
        mCurrentTimeTextView=(TextView)findViewById(R.id.scheduleTv);
        mTotalTimeTextView=(TextView)findViewById(R.id.durationTv);
        mProgressBar=(SeekBar) findViewById(R.id.player_seek);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, Object... objects) {
        return setUp(url, cacheWithPlay, (File) null, objects);
    }

    @Override
    public void startPlayLogic() {
        prepareVideo();
    }


    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                updateStartImg();
                break;
            case CURRENT_STATE_PREPAREING:
                updateStartImg();
                break;
            case CURRENT_STATE_PLAYING:
                updateStartImg();
                break;
            case CURRENT_STATE_PAUSE:
                updateStartImg();
                break;
            case CURRENT_STATE_ERROR:
                Log.d(TAG, "setStateAndUi: STATE_ERROR");
                Toast.makeText(context, "播放链接失效了", Toast.LENGTH_SHORT).show();
                updateStartImg();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                updateStartImg();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                updateStartImg();
                break;
        }
    }

    @Override
    protected void onClickUiToggle() {

    }


    public void updateStartImg(){
        if(mCurrentState==CURRENT_STATE_PLAYING){
            mStartButton.setImageResource(R.drawable.music_pause);
        }else{
            mStartButton.setImageResource(R.drawable.music_start);
        }

    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if (progress != 0) mProgressBar.setProgress(progress);
        if (secProgress != 0 && !mCacheFile) mProgressBar.setSecondaryProgress(secProgress);
    }

    @Override
    protected void resetProgressAndTime() {
        super.resetProgressAndTime();
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);
    }

    @Override
    public void onVideoSizeChanged() {

    }
}
