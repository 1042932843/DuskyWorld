/**
 * Copyright (C), 1995-2018, Dusky
 * FileName: SQLiteDbHelper
 * Author: Dusky
 * Date: 2018/12/20 18:48
 * Description: 创建一个继承自 SQLiteOpenHelper 的子类来管理数据库的创建、升级的工具类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.imageviewer.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ClassName: SQLiteDbHelper
 * @Description: java类作用描述
 * @Author: Dusky
 * @Date: 2018/12/20 18:48
 */
public class SQLiteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dwimg.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_IMG = "img";

    //创建表的 sql 语句
    private static final String IMG_CREATE_TABLE_SQL =
            "create TABLE " + TABLE_IMG + "("
            + "id integer primary key autoincrement,"
            + "name varchar not null,"
            + "path varchar not null,"
            + "type varchar not null,"
            + "addTime long not null,"
            + "width int not null,"
            + "height int not null,"
            + "size varchar not null"
            + ");";

    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        db.execSQL(IMG_CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        switch (oldVersion) {
            case 1:
                // do something
                break;

            default:
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // 启动外键
            db.execSQL("PRAGMA foreign_keys = 1;");
        }
    }

}