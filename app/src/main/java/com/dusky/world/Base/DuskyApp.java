package com.dusky.world.Base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;


import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dusky.imageviewer.ImageViewer;
import com.dusky.world.Module.activities.HomePage;
import com.dusky.world.R;
import com.dusky.world.Utils.GlideImageLoader;
import com.dusky.world.Utils.GlideTransformation.Reflected;
import com.dusky.world.Utils.PreferenceUtil;
import com.dusky.world.Utils.ToastUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;



/**
 * Name: DuskyApp
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2018-05-03 10:00
 */

public class DuskyApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{
    public static DuskyApp mInstance;
    public static RequestOptions optionsRoundedCorners,optionsRoundedCircle,optionsReflected;
    private ArrayList<WeakReference<Activity>> activities=new ArrayList<>();
    public static DuskyApp getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //LeakCanary.install(this);
        ImageViewer.getInstance().setImageLoader(new GlideImageLoader());

        init();
        initBugly();
        registerActivityLifecycleCallbacks(this);

    }
    /**\
     * 如果App使用了多进程且各个进程都会初始化Bugly（例如在Application类onCreate()中初始化Bugly），那么每个进程下的Bugly都会进行数据上报，造成不必要的资源浪费。
     因此，为了节省流量、内存等资源，建议初始化的时候对上报进程进行控制，只在主进程下上报数据：判断是否是主进程（通过进程名是否为包名来判断），并在初始化Bugly时增加一个上报进程的策略配置。
     */
    private void initBugly() {
        /*Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "d9d9e8dc08", true, strategy);*/
        Beta.smallIconId = R.mipmap.ic_launcher;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Beta.canShowUpgradeActs.add(HomePage.class);
        Bugly.init(getApplicationContext(), "d9d9e8dc08", true);

    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
    }


    private void init() {

        //配置glide圆角
        optionsRoundedCorners  = new RequestOptions()
                .centerCrop()
                //.error(R.drawable.zhanweitu)
                .priority(Priority.HIGH)
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //头像圆形配置
        optionsRoundedCircle  = new RequestOptions()
                .centerCrop()
                //.error(R.drawable.zhanweitu)
                .priority(Priority.HIGH)
                .transform(new CircleCrop())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //配置倒影
        optionsReflected = new RequestOptions()
                .centerCrop()
                //.error(R.drawable.zhanweitu)
                .priority(Priority.HIGH)
                .transform(new Reflected())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    }



    public void logout(){
        PreferenceUtil.resetPrivate();
        ToastUtil.ShortToast("已退出，请重新登录");

    }
    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            appConfig.versionName = pi.versionName;
            appConfig.versioncode = pi.versionCode;
            versionName=appConfig.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        WeakReference<Activity> activityWeakReference=new WeakReference<Activity>(activity);
        activities.add(activityWeakReference);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }

    public void Exit(){
        int size=activities.size();
        for(int i=0;i<size;i++){
            if(activities.get(i)!=null){
                 activities.get(i).get().finish();
            }
        }
    }

}