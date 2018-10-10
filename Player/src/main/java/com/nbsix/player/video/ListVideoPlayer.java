package com.nbsix.player.video;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.nbsix.player.VideoPlayer;
import com.nbsix.player.model.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListVideoPlayer extends StandardVideoPlayer {

    protected List<VideoModel> mUriList = new ArrayList<>();
    protected int mPlayPosition;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public ListVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public ListVideoPlayer(Context context) {
        super(context);
    }

    public ListVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects       object[0]目前为title
     * @return
     */
    public boolean setUp(List<VideoModel> url, boolean cacheWithPlay, int position, Object... objects) {
        mUriList = url;
        mPlayPosition = position;
        VideoModel videoModel = url.get(position);
        boolean set = setUp(videoModel.getUrl(), cacheWithPlay, objects);
        if (!TextUtils.isEmpty(videoModel.getTitle())) {
            mTitleTextView.setText(videoModel.getTitle());
        }
        return set;
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
    public boolean setUp(List<VideoModel> url, boolean cacheWithPlay, int position, File cachePath, Object... objects) {
        mUriList = url;
        mPlayPosition = position;
        VideoModel videoModel = url.get(position);
        boolean set = setUp(videoModel.getUrl(), cacheWithPlay, cachePath, objects);
        if (!TextUtils.isEmpty(videoModel.getTitle())) {
            mTitleTextView.setText(videoModel.getTitle());
        }
        return set;
    }

    @Override
    public BaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        BaseVideoPlayer baseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if (baseVideoPlayer != null) {
            ListVideoPlayer listGSYVideoPlayer = (ListVideoPlayer) baseVideoPlayer;
            listGSYVideoPlayer.mPlayPosition = mPlayPosition;
            listGSYVideoPlayer.mUriList = mUriList;
            VideoModel videoModel = mUriList.get(mPlayPosition);
            if (!TextUtils.isEmpty(videoModel.getTitle())) {
                listGSYVideoPlayer.mTitleTextView.setText(videoModel.getTitle());
            }
        }
        return baseVideoPlayer;
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, VideoPlayer gsyVideoPlayer) {
        if (gsyVideoPlayer != null) {
            ListVideoPlayer listGSYVideoPlayer = (ListVideoPlayer) gsyVideoPlayer;
            mPlayPosition = listGSYVideoPlayer.mPlayPosition;
            mUriList = listGSYVideoPlayer.mUriList;
            VideoModel videoModel = mUriList.get(mPlayPosition);
            if (!TextUtils.isEmpty(videoModel.getTitle())) {
                mTitleTextView.setText(videoModel.getTitle());
            }
        }
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    @Override
    public void onCompletion() {
        if (mPlayPosition < (mUriList.size() - 1)) {
            return;
        }
        super.onCompletion();
    }

    @Override
    public void onAutoCompletion() {
        if (mPlayPosition < (mUriList.size() - 1)) {
            mPlayPosition++;
            VideoModel videoModel = mUriList.get(mPlayPosition);
            setUp(videoModel.getUrl(), mCache, mCachePath, mObjects);
            if (!TextUtils.isEmpty(videoModel.getTitle())) {
                mTitleTextView.setText(videoModel.getTitle());
            }
            startPlayLogic();
            return;
        }
        super.onAutoCompletion();
    }

}
