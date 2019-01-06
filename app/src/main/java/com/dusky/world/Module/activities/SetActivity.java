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
import android.support.v7.widget.Toolbar;

import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;

import butterknife.BindView;

/**
 * @ClassName: SetActivity
 * @Description: 设置页面
 * @Author: dusky
 * @Date: 2019/1/6 19:36
 */
public class SetActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle("设置");
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
