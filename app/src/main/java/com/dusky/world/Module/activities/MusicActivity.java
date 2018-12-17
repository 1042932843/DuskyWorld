package com.dusky.world.Module.activities;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Base.DuskyApp;
import com.dusky.world.Design.MusicPlayer.AlbumViewPager;
import com.dusky.world.Design.MusicPlayer.PlayerSeekBar;
import com.dusky.world.Design.lrc.LrcView;
import com.dusky.world.Module.entity.MusicInfo;
import com.dusky.world.Module.fragment.PlayQueueFragment;
import com.dusky.world.R;

import butterknife.BindView;


/**
 * Created by wm on 2016/2/21.
 */
public class MusicActivity extends BaseActivity{


    MusicInfo  musicInfo;

    @BindView(R.id.tragetlrc)
    TextView mTryGetLrc;
    @BindView(R.id.music_duration_played)
    TextView mTimePlayed;
    @BindView(R.id.music_duration)
    TextView mDuration;
    @BindView(R.id.playing_playlist)
    ImageView mPlaylist;
    @BindView(R.id.playing_mode)
    ImageView mPlayingmode;
    @BindView(R.id.playing_play)
    ImageView mControl;
    @BindView(R.id.playing_next)
    ImageView mNext;
    @BindView(R.id.playing_pre)
    ImageView mPre;

    @BindView(R.id.albumArt)
    ImageView mBackAlbum;
    @BindView(R.id.needle)
    ImageView mNeedle;
    @BindView(R.id.headerView)
    FrameLayout mAlbumLayout;
    @BindView(R.id.lrcviewContainer)
    RelativeLayout mLrcViewContainer;
    @BindView(R.id.music_tool)
    LinearLayout mMusicTool;
    @BindView(R.id.lrcview)
    LrcView mLrcView;
    @BindView(R.id.volume_seek)
    SeekBar mVolumeSeek;
    @BindView(R.id.play_seek)
    PlayerSeekBar mProgress;
    @BindView(R.id.view_pager)
    AlbumViewPager mViewPager;

    private ObjectAnimator mNeedleAnim, mRotateAnim;
    private AnimatorSet mAnimatorSet;
    private String TAG = MusicActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        musicInfo= getIntent().getParcelableExtra("music");
        Glide.with(this).load(musicInfo.albumData).into(mBackAlbum);
        mNeedleAnim = ObjectAnimator.ofFloat(mNeedle, "rotation", -25, 0);
        mNeedleAnim.setDuration(200);
        mNeedleAnim.setRepeatMode(ValueAnimator.RESTART);
        mNeedleAnim.setInterpolator(new LinearInterpolator());

        mProgress.setIndeterminate(false);
        mProgress.setProgress(1);
        mProgress.setMax(1000);
        loadOther();
        initLrcView();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    private void initLrcView() {
        mLrcView.setOnSeekToListener(onSeekToListener);
        mLrcView.setOnLrcClickListener(onLrcClickListener);
        mViewPager.setOnSingleTouchListener(new AlbumViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View v) {
                if (mAlbumLayout.getVisibility() == View.VISIBLE) {
                    mAlbumLayout.setVisibility(View.INVISIBLE);
                    mLrcViewContainer.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.INVISIBLE);
                }
            }
        });
        mLrcViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                    mLrcViewContainer.setVisibility(View.INVISIBLE);
                    mAlbumLayout.setVisibility(View.VISIBLE);
                    mMusicTool.setVisibility(View.VISIBLE);
                }
            }
        });

        mTryGetLrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int mMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolumeSeek.setMax(mMaxVol);
        mVolumeSeek.setProgress(v);
        mVolumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.ADJUST_SAME);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    LrcView.OnLrcClickListener onLrcClickListener = new LrcView.OnLrcClickListener() {

        @Override
        public void onClick() {

            if (mLrcViewContainer.getVisibility() == View.VISIBLE) {
                mLrcViewContainer.setVisibility(View.INVISIBLE);
                mAlbumLayout.setVisibility(View.VISIBLE);
                mMusicTool.setVisibility(View.VISIBLE);
            }
        }
    };
    LrcView.OnSeekToListener onSeekToListener = new LrcView.OnSeekToListener() {

        @Override
        public void onSeekTo(int progress) {
            //MusicPlayer.seek(progress);
        }
    };

    /*


    private List<LrcRow> getLrcRows() {

        List<LrcRow> rows = null;
        InputStream is = null;
        try {
            is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +"/remusic/lrc/" + MusicPlayer.getCurrentAudioId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is == null) {
                return null;
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }
*/
    private void loadOther() {
        setSeekBarListener();
        setTools();
    }


    private void setTools() {


        mPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayQueueFragment playQueueFragment = new PlayQueueFragment();
                playQueueFragment.show(getSupportFragmentManager(), "playlistframent");
            }
        });




    }





    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void setSeekBarListener() {

        if (mProgress != null)
            mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
    }

    private void stopAnim() {

        if (mRotateAnim != null) {
            mRotateAnim.end();
            mRotateAnim = null;
        }
        if (mNeedleAnim != null) {
            mNeedleAnim.end();
            mNeedleAnim = null;
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopAnim();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopAnim();
    }



}