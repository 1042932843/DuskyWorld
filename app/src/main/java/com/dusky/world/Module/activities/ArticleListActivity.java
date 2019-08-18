/**
 * Copyright (C), 1995-2019, DUSKY
 * FileName: ArticleListActivity
 * Author: dusky
 * Date: 2019/1/6 19:36
 * Description: 文章列表页
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.dusky.world.Module.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dusky.world.Adapter.ArticleListAdapter;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Design.helper.EndlessRecyclerOnScrollListener;
import com.dusky.world.Module.entity.ArticleItem;
import com.dusky.world.Module.entity.DefaultType;
import com.dusky.world.Module.entity.User;
import com.dusky.world.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @ClassName: ArticleListActivity
 * @Description: 文章列表页
 * @Author: dusky
 * @Date: 2019/8/13 11:55
 */
public class ArticleListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView_article)
    RecyclerView recyclerView;

    private ArrayList<ArticleItem> articleItems=new ArrayList<>();
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    ArticleListAdapter articleListAdapter;
    private boolean mIsRefreshing = false;
    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_article;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle("更多文章");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        initRecyclerView();
    }

    public void initRecyclerView(){
        for (int i=0;i<10;i++){
            User user=new User("dusky","10423932843","http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg","500");
            articleItems.add(new ArticleItem("Title"+i,"http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg",getString(R.string.tip),"手机游戏",i+"阅读",i*99+"回复",i*88+"喜欢",user));
        }
        articleListAdapter=new ArticleListAdapter(this,articleItems);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleListAdapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @SuppressLint("CheckResult")
            @Override
            public void onLoadMore(int currentPage) {
                loadListData();
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    public void loadListData(){
        for (int i=0;i<10;i++){
            User user=new User("dusky","10423932843","http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg","500");
            articleItems.add(new ArticleItem("Title"+i,"http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg",getString(R.string.tip),"手机游戏",i+"阅读",i*99+"回复",i*88+"喜欢",user));
        }
        pageNum++;
        articleListAdapter.Refresh(pageNum*pageSize,pageNum);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
