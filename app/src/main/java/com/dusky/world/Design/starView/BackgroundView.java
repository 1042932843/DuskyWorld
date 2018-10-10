package com.dusky.world.Design.starView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Name: BackgroundView
 * Author: Dusky
 * QQ: 1042932843
 * Comment: canvas中绘图练习
 * Date: 2017-05-06 12:06
 */

public class BackgroundView extends View{

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    private int lineCount = 64;//屏幕出现的点数量
    private int CircleSize = 1;//点大小

    private int canvas_width=0;
    private int canvas_height=0;

    private List<LineConfig> random_lines = new ArrayList<>();//屏幕出现的点集合


    private int color_point = Color.rgb(255,215,0);//点的颜色
    private int color_line = Color.argb(60,95,158,160);//线的颜色


    public BackgroundView(Context context) {
        super(context);


    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //注意取消监听，因为该方法会调用多次！
                if(BackgroundView.this.getWidth()!=0){
                    init();
                    BackgroundView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });
    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    public void init(){
        canvas_height = this.getHeight();
        canvas_width = this.getWidth();

        /*初始化点集合*/
        for(int i=0; i < lineCount ; ++i){
            LineConfig l = new LineConfig();
            l.x = (float) (Math.random() * canvas_width);
            l.y = (float) (Math.random() * canvas_height);
            l.s = CircleSize;
            l.xa = (float) (Math.random() * 1.6 -0.8);
            l.ya = (float) (Math.random() * 1.6 -0.8);
            l.max = 15000;

            random_lines.add(l);
        }

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint_blue = new Paint();
        paint_blue.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_blue.setColor(color_point);
        paint_blue.setStrokeWidth(2);

        for(int i = 0; i < random_lines.size() ; i ++ ){
            LineConfig r = random_lines.get(i);
            r.x += r.xa;
            r.y += r.ya; //移动
            r.xa *= r.x >= canvas_width || r.x < 0 ? -1 : 1;
            r.ya *= r.y >= canvas_height || r.y < 0 ? -1 : 1; //碰到边界，反向反弹

            int alpha=(int)((1-r.y/canvas_height)*180)+75;//+100;//补正100的透明度
            //alpha= (int)(Math.random()*(alpha-50)+50);
            paint_blue.setAlpha(alpha);
            r.s=(int)((1-r.y/canvas_height)*2)+1;//+1;//补正最低为1
            canvas.drawCircle(r.x, r.y, r.s, paint_blue);

            /*for(int j = i + 1; j < random_lines.size() ; j ++ ){
                LineConfig e = random_lines.get(j);
                x_dist = r.x - e.x; //x轴距离
                y_dist = r.y - e.y; //y轴距离
                dist = x_dist * x_dist + y_dist * y_dist; //总距离
                if(dist < e.max){
                    if(e.touch && dist >= e.max / 2){
                        r.x -= 0.02 * x_dist;
                        r.y -= 0.02 * y_dist; //靠近的时候加速
                    }

                    paint_blue.setColor(color_line);
                    canvas.drawLine(r.x,r.y,e.x,e.y,paint_blue);
                }
            }*/
        }

        new DrawThread().start();//启动定时线程绘制
    }


    /**
     * 定时通知绘制线程
     * */
    private class DrawThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                sleep(1000 / 30);//每秒绘制30次
                mHandler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 闪烁计时线程
     * */
    private long changeTime=0;
    private class blinThread extends Thread {

        @Override
        public void run() {
            super.run();
            long timec=System.currentTimeMillis()-changeTime;
            if(timec>1000){
                changeTime=System.currentTimeMillis();
            }else {
                if(timec<500){
                    for(int i = 0; i < random_lines.size() ; i ++ ){
                        LineConfig r = random_lines.get(i);
                        r.s=(int)(timec/100)+1;
                    }
                }else {
                    for(int i = 0; i < random_lines.size() ; i ++ ){
                        LineConfig r = random_lines.get(i);
                        r.s=(int)(10-timec/100)+1;
                    }
                }

            }


        }
    }



    /**绘图通知handler*/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) draw_canvas();
        }
    };

    /**
     * 重绘视图通知
     */
    private void draw_canvas() {
        this.invalidate();
    }

    /**
     * 屏幕点对象
     * */
    private class LineConfig{
        public float x;//x左标
        public float y;//y左标
        public float xa;//x增量
        public float ya;//y增量
        public float max;//两点间最大距离，超过此距离不再绘制线段
        public int s;//大小
        public boolean touch = false;//标示是否为触摸点

    }

}
