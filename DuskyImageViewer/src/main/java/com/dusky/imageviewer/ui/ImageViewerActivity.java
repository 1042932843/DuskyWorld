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

import com.dusky.imageviewer.ImageViewer;
import com.dusky.imageviewer.R;
import com.dusky.imageviewer.adapter.ImageViewerAdapter;
import com.dusky.imageviewer.bean.ImageItem;
import com.dusky.imageviewer.util.ImageDataSource;
import com.dusky.imageviewer.util.SystemBarHelper;

import java.util.ArrayList;

public class ImageViewerActivity extends AppCompatActivity implements ImageDataSource.OnImagesLoadedListener{
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;

    Toolbar toolbar;
    ViewPager viewPager;
    ImageViewerAdapter imageViewerAdapter;
    ArrayList<ImageItem> images=new ArrayList<>();
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
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
}
