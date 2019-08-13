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

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;

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



    @Override
    public int getLayoutId() {
        return R.layout.activity_list_article;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
