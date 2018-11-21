package com.dusky.duskyplayer.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dusky.duskyplayer.R;
import com.dusky.duskyplayer.listener.LockClickListener;
import com.dusky.duskyplayer.listener.StandardVideoAllCallBack;
import com.dusky.duskyplayer.utils.CommonUtil;
import com.dusky.duskyplayer.utils.NetworkUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IjkLibLoader;

import static com.dusky.duskyplayer.utils.CommonUtil.hideNavKey;


/**
 * 标准播放器
 */

public class StandardADVideoPlayer extends ADVideoPlayer {


    public StandardADVideoPlayer(Context context) {
        super(context);
    }

    public StandardADVideoPlayer(Context context, IjkLibLoader ijkLibLoader) {
        super(context, ijkLibLoader);
    }

    public StandardADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_ad;
    }

    @Override
    public void startPlayLogic() {
        prepareVideo();
    }


    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoResume() {

    }
}
