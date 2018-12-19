/**
 * Copyright (C), 1995-2018, Dusky
 * FileName: ImageViewerAdapter
 * Author: Dusky
 * Date: 2018/12/19 17:40
 * Description: adapter for the simple img
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.imageviewer.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dusky.imageviewer.ImageViewer;
import com.dusky.imageviewer.bean.ImageItem;
import com.dusky.imageviewer.util.Utils;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;

/**
 * @ClassName: ImageViewerAdapter
 * @Description: adapter
 * @Author: Dusky
 * @Date: 2018/12/19 17:40
 */
public class ImageViewerAdapter extends PagerAdapter {
    private int screenWidth;
    private int screenHeight;
    private ArrayList<ImageItem> images = new ArrayList<>();
    private Activity mActivity;
    private PhotoViewClickListener listener;
    private ImageViewer viewer;

    public ImageViewerAdapter(Activity activity, ArrayList<ImageItem> images) {
        this.mActivity = activity;
        this.images = images;
        this.viewer= ImageViewer.getInstance();

        DisplayMetrics dm = Utils.getScreenPix(activity);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public void setOnPhotoTapListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        ImageItem imageItem = images.get(position);
        viewer.getImageLoader().displayImage(mActivity, imageItem.path, photoView, screenWidth, screenHeight);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) listener.OnPhotoTapListener(view, x, y);
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
