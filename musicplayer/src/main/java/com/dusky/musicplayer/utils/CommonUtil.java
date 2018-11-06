package com.dusky.musicplayer.utils;

import android.os.Environment;
import java.io.File;
import java.util.Formatter;
import java.util.Locale;

/**
 * 工具类
 */

public class CommonUtil {

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    
    /**
     * 下载速度文本
     */
    public static String getTextSpeed(long speed) {
        String text = "";
        if (speed >= 0 && speed < 1024) {
            text = speed + " KB/s";
        } else if (speed >= 1024 && speed < (1024 * 1024)) {
            text = Long.toString(speed / 1024) + " KB/s";
        } else if (speed >= (1024 * 1024) && speed < (1024 * 1024 * 1024)) {
            text = Long.toString(speed / (1024 * 1024)) + " MB/s";
        }
        return text;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }


    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    public static final String NAME = "Media";


    public static String getAppPath(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(SD_PATH);
        sb.append(File.separator);
        sb.append(name);
        sb.append(File.separator);
        return sb.toString();
    }

    public static String getPath() {
        return getAppPath(NAME);
    }

    public static void deleteFiles(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory() && f.exists()) { // 判断是否存在
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
