package com.dusky.imageviewer.loader;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

public interface ImageLoader extends Serializable {

    void displayImage(Activity activity,String  path,ImageView imageView);

    void clearMemoryCache();
}
