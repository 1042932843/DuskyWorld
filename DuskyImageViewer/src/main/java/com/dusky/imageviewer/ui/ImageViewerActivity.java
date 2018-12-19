package com.dusky.imageviewer.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.dusky.imageviewer.ImageViewer;
import com.dusky.imageviewer.R;
import com.dusky.imageviewer.adapter.ImageViewerAdapter;
import com.dusky.imageviewer.bean.ImageItem;
import com.dusky.imageviewer.util.SystemBarHelper;

import java.util.ArrayList;

public class ImageViewerActivity extends AppCompatActivity {
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
}
