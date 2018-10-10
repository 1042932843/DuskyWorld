package com.dusky.world.Design.MusicPlayer;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dusky.world.R;


/**
 * Created by wm on 2016/12/29.
 */
public class PlayerSeekBar extends android.support.v7.widget.AppCompatSeekBar {


    public PlayerSeekBar(Context context) {
        super(context);
    }

    public PlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setThumb(getResources().getDrawable(R.drawable.play_plybar_btn));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }



}
