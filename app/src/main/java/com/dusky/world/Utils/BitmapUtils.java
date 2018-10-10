package com.dusky.world.Utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Name: BitmapUtils
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-07-12 15:48
 */

public  class BitmapUtils {
    public static AssetManager assets;
    /**
     * 传入图片名和是否需要透明处理
     * @param s
     * @param b
     * @return
     */
    public static Bitmap loadBitmap(String s, boolean b) {
        InputStream inputStream=null;
        try {
            inputStream= assets.open(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options op=new BitmapFactory.Options();
        if(b){
            op.inPreferredConfig= Bitmap.Config.ARGB_8888;
        }else {
            op.inPreferredConfig= Bitmap.Config.RGB_565;
        }
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream,null,op);
        return bitmap;
    }


    public static Bitmap convertBmp(Bitmap bmp){
        int w = bmp.getWidth();
        int h = bmp.getHeight()/3;

        Bitmap convertBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(convertBmp);
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1); //镜像垂直翻转
//  matrix.postScale(-1, 1); //镜像水平翻转
//  matrix.postRotate(-90); //旋转-90度

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
        cv.drawBitmap(newBmp, new Rect(0, 0,newBmp.getWidth(), newBmp.getHeight()),new Rect(0, 0, w, h), null);
        return convertBmp;
    }



    public static Bitmap createReflectedBitmap(Bitmap resourceBitmap) {
       int ReflectionGap = 6;//原图片于倒影之间的距离
            //源图片
            int width = resourceBitmap.getWidth();
            int height = resourceBitmap.getHeight();
            //带有倒影的图片
            Bitmap bitmap = Bitmap.createBitmap(width, height+ReflectionGap+ (int)(height*0.3f), Bitmap.Config.ARGB_8888);
            //创建画布
            Canvas canvas = new Canvas(bitmap);
            //绘制源图片
            canvas.drawBitmap(resourceBitmap, 0, 0, null);
            //绘制倒影图片
            canvas.drawBitmap(createReflectedBitmap(resourceBitmap,0.3f), 0, height + ReflectionGap,null);

        return bitmap;
	}


    public static Bitmap createReflectedBitmap(Bitmap srcBitmap,
                                               float reflectHeight) {
        if (null == srcBitmap) {
            return null;
        }

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = reflectHeight == 0 ? srcHeight / 3
                : (int) (reflectHeight * srcHeight);

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            // The reflection bitmap, width is same with original's
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight - reflectionHeight, reflectionWidth,
                    reflectionHeight, matrix, false);

            if (null == reflectionBitmap) {
                return null;
            }

            Canvas canvas = new Canvas(reflectionBitmap);

            Paint paint = new Paint();
            paint.setAntiAlias(true);

            LinearGradient shader = new LinearGradient(0, 0, 0,
                    reflectionBitmap.getHeight(), 0x99FFFFFF, 0x00FFFFFF,
                    Shader.TileMode.MIRROR);
            paint.setShader(shader);

            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));

            // Draw the linear shader.
            canvas.drawRect(0, 0, reflectionBitmap.getWidth(),
                    reflectionBitmap.getHeight(), paint);

            return reflectionBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



}
