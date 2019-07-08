/**
 * Copyright (C), 1995-2019, DUSKY
 * FileName: SetActivity
 * Author: dusky
 * Date: 2019/1/6 19:36
 * Description: 对app进行各种设置
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.world.Module.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dusky.world.Adapter.TimeAxisAdapter;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Design.helper.ItemDecoration;
import com.dusky.world.Module.entity.TimeAxisItem;
import com.dusky.world.R;
import com.nbsix.dsy.timeAxis.DotItemDecoration;
import com.nbsix.dsy.timeAxis.SpanIndexListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @ClassName: TimeAxisActivity
 * @Description: 时间轴
 * @Author: dusky
 * @Date: 2019/7/8 10:36
 */
public class TimeAxisActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView_timeaxis)
    RecyclerView recyclerView;

    TimeAxisAdapter timeAxisAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_timeaxis;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle("时间轴");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initRecyclerView();

    }

    public void initRecyclerView(){

        DotItemDecoration  mItemDecoration = new DotItemDecoration
                .Builder(this)
                .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
                .setItemStyle(DotItemDecoration.STYLE_DRAW)
                .setTopDistance(64)//dp
                .setItemInterVal(72)//dp
                .setItemPaddingLeft(24)//default value equals to item interval value
                .setItemPaddingRight(24)//default value equals to item interval value
                .setDotColor(Color.WHITE)
                .setDotRadius(3)//dp
                .setDotPaddingTop(0)
                .setDotInItemOrientationCenter(true)//set true if you want the dot align center
                .setLineColor(ContextCompat.getColor(this,R.color.gray_light))
                .setLineWidth(1)//dp
                .setEndText("END")
                .setTextColor(Color.WHITE)
                .setTextSize(10)//sp
                .setDotPaddingText(2)//dp.The distance between the last dot and the end text
                .setBottomDistance(40)//you can add a distance to make bottom line longer
                .create();


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        List<TimeAxisItem> items=new ArrayList<>();
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","小猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","大猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","野猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","公猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","2019-07-08","999次播放","母猪佩奇"));
        timeAxisAdapter=new TimeAxisAdapter(TimeAxisActivity.this,items);
        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.setAdapter(timeAxisAdapter);


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
