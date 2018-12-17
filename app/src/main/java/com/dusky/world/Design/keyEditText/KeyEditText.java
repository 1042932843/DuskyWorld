
package com.dusky.world.Design.keyEditText;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;


/**
 * @author xyczero
 * @time 16/5/23
 */
public class KeyEditText extends AppCompatEditText {
    private KeyPreImeListener mKeyPreImeListener;

    public KeyEditText(Context context) {
        super(context);
    }

    public KeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (mKeyPreImeListener != null) {
                    mKeyPreImeListener.onKeyPreImeUp(keyCode, event);
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setKeyPreImeListener(KeyPreImeListener listener) {
        mKeyPreImeListener = listener;
    }

    public interface KeyPreImeListener {
        void onKeyPreImeUp(int keyCode, KeyEvent event);
    }
}
