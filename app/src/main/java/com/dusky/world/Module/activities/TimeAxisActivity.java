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

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.dusky.world.Adapter.TimeAxisAdapter;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Design.helper.ItemDecoration;
import com.dusky.world.Module.entity.TimeAxisItem;
import com.dusky.world.R;

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        List<TimeAxisItem> items=new ArrayList<>();
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","测试1"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","测试2"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","测试3"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","测试4"));
        items.add(new TimeAxisItem("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg","测试5"));
        timeAxisAdapter=new TimeAxisAdapter(TimeAxisActivity.this,items);
        recyclerView.addItemDecoration(new ItemDecoration(TimeAxisActivity.this,100));
        recyclerView.setAdapter(timeAxisAdapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
