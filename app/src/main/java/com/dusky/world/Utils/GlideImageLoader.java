package com.dusky.world.Utils;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dusky.imageviewer.loader.ImageLoader;

import java.io.File;


public class GlideImageLoader implements ImageLoader {
    int width;
    int height;

    public GlideImageLoader(){

    }

    //自行实现构造方法传入需要的配置参数
    public GlideImageLoader(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void displayImage(Activity activity,String  path,ImageView imageView) {
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
