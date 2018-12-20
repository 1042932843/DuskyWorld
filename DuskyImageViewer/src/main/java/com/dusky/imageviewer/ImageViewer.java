/**
 * Copyright (C), 1995-2018, Dusky
 * FileName: ImageViewer
 * Author: Dusky
 * Date: 2018/12/19 17:50
 * Description: 主类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.imageviewer;

import com.dusky.imageviewer.bean.ImageFolder;
import com.dusky.imageviewer.loader.ImageLoader;

import java.util.List;

/**
 * @ClassName: ImageViewer
 * @Description: java类作用描述
 * @Author: Dusky
 * @Date: 2018/12/19 17:50
 */
public class ImageViewer {

    public static final int CODE_BACK = 1005;

    public static final String IMAGE_LIST="images";

    private static ImageViewer mInstance;
    public static ImageViewer getInstance() {
        if (mInstance == null) {
            synchronized (ImageViewer.class) {
                if (mInstance == null) {
                    mInstance = new ImageViewer();
                }
            }
        }
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    ImageLoader imageLoader;

    public static ImageViewer getmInstance() {
        return mInstance;
    }

    public static void setmInstance(ImageViewer mInstance) {
        ImageViewer.mInstance = mInstance;
    }



}
