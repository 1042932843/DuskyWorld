package com.dusky.world.Module.activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Base.DuskyApp;
import com.dusky.world.R;
import com.dusky.world.Utils.QRCode.AwesomeQRCode;
import com.dusky.world.Utils.SystemBarHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Name: UserInfoActivity
 * Author: Dusky
 * QQ: 1042932843
 * Comment: 用户信息页面
 * Date: 2017-07-20 15:33
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_avatar_view)
    ImageView user_info_avatar_view;
    @BindView(R.id.user_info_avatar_qr)
    ImageView user_info_avatar_qr;
    @BindView(R.id.user_info_toolbar)
    Toolbar user_info_toolbar;

    boolean Enlarge = false;

    @OnClick(R.id.user_info_avatar_qr)
    public void initQR() {
        if (!Enlarge) {
            EnlargeQR();
            Enlarge = true;
        } else {
            NarrowQR();
            Enlarge = false;
        }
    }


    public void InitAvatatrANDQR() {
        Glide.with(this).load("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg").apply(DuskyApp.optionsRoundedCircle).into(user_info_avatar_view);
        Glide.with(this).asBitmap().load("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg").into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                generateQR(resource);
            }

        });
    }

    public void EnlargeQR() {
        Animation scaleAnimation = new ScaleAnimation(1f, 15f, 1f, 15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        user_info_avatar_qr.startAnimation(scaleAnimation);
    }
    public void NarrowQR() {
        Animation scaleAnimation = new ScaleAnimation(15f, 1f, 15f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);
        user_info_avatar_qr.startAnimation(scaleAnimation);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void init(Bundle savedInstanceState) {
        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setHeightAndPadding(this, user_info_toolbar);
        user_info_toolbar.setTitle("");
        setSupportActionBar(user_info_toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        InitAvatatrANDQR();
    }


    private void generateQR(Bitmap bitmap) {

        /**
         * 参数说明
         参数名	类型	说明	默认值	备注
         contents	String	欲编码的内容	null	必需
         size	int-px	尺寸, 长宽一致, 包含外边距	800	必需
         margin	int-px	二维码图像的外边距	20	必需
         dataDotScale	float	数据点缩小比例	0.3f	(0, 1.0f)
         colorDark	int-color	非空白区域的颜色	Color.BLACK
         colorLight	int-color	空白区域的颜色	Color.WHITE
         background	Bitmap	欲嵌入的背景图, 设为 null 以禁用	null
         whiteMargin	int-px	若设为 true, 背景图外将绘制白色边框	true
         autoColor	boolean	若为 true, 背景图的主要颜色将作为实点的颜色, 即 colorDark	true
         binarize	boolean	若为 true, 图像将被二值化处理, 未指定阈值则使用默认值	fasle
         binarizeThreshold	int	二值化处理的阈值	128	(0, 255)
         roundedDataDots	boolean	若为 true, 数据点将以圆点绘制	false
         logo	Bitmap	欲嵌入至二维码中心的 Logo, 设为 null 以禁用	null
         logoMargin	int-px	Logo 周围的空白边框, 设为 0 以禁用	10
         logoCornerRadius	int-px	Logo 及其边框的圆角半径, 设为 0 以禁用	8
         logoScale	float	用于计算 Logo 大小, 过大将覆盖过多数据点而导致解码失败	0.2f	(0, 1.0f)
         */
        new AwesomeQRCode.Renderer()
                .contents("www.baidu.com")
                .size(800).margin(20)
                .background(bitmap)
                .autoColor(true)
                .whiteMargin(true)
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, final Bitmap bitmap) {
                        runOnUiThread(() -> {
                            // 提示: 这里使用 runOnUiThread(...) 来规避从非 UI 线程操作 UI 控件时产生的问题。
                            user_info_avatar_qr.setImageBitmap(bitmap);
                        });
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                        e.printStackTrace();
                    }
                });
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!Enlarge) {
        } else {
            NarrowQR();
            Enlarge = false;
        }
        return true;
    }
}

