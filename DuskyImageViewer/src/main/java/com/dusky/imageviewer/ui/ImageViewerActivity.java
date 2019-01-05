package com.dusky.imageviewer.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dusky.imageviewer.ImageViewer;
import com.dusky.imageviewer.R;
import com.dusky.imageviewer.adapter.ImageViewerAdapter;
import com.dusky.imageviewer.bean.ImageItem;
import com.dusky.imageviewer.util.BiliDanmukuParser;
import com.dusky.imageviewer.util.ImageDataSource;
import com.dusky.imageviewer.util.SystemBarHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;

import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

public class ImageViewerActivity extends AppCompatActivity implements ImageDataSource.OnImagesLoadedListener{
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;

    DanmakuView danmakuView;
    private BaseDanmakuParser mParser;
    Toolbar toolbar;
    ViewPager viewPager;
    ImageViewerAdapter imageViewerAdapter;
    ArrayList<ImageItem> images=new ArrayList<>();
    DanmakuContext mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置StatusBar透明
        SystemBarHelper.immersiveStatusBar(this);
        setContentView(R.layout.activity_image_viewer);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        imageViewerAdapter=new ImageViewerAdapter(this,images);
        viewPager.setAdapter(imageViewerAdapter);
        SystemBarHelper.setHeightAndPadding(this, toolbar);
        //toolbar.setTitle("");
        toolbar.setTitle("返回");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            new ImageDataSource(this, null, this);
        }
        initDanmaku();

    }

    private void initDanmaku(){
        danmakuView=(DanmakuView)findViewById(R.id.sv_danmaku);
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
  //              .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);

        if (danmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            danmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    danmakuView.start();
                }
            });
            /*danmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });*/
            danmakuView.prepare(mParser, mContext);
            danmakuView.showFPS(false);
            danmakuView.enableDanmakuDrawingCache(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (danmakuView != null) {
            // dont forget release!
            danmakuView.release();
            danmakuView = null;
        }
        Intent intent = new Intent();
        setResult(ImageViewer.CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }


    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onImagesLoaded(ArrayList<ImageItem> list) {
        images.addAll(list);
        imageViewerAdapter.notifyDataSetChanged();
    }


    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (danmakuView != null) {
            // dont forget release!
            danmakuView.release();
            danmakuView = null;
        }
    }

}
