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
import android.widget.Button;

import com.dusky.world.Base.BaseActivity;
import com.dusky.world.R;
import com.dusky.world.Utils.ToastUtil;
import com.readboy.onestroke.LinesInfo;
import com.readboy.onestroke.OneStrokeView;
import com.readboy.onestroke.Point;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @ClassName: TestHomeActivity
 * @Description: 实验室
 * @Author: dusky
 * @Date: 2019/8/29 10:43
 */
public class TestHomeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.one_stroke)
    OneStrokeView strokeView;

    @BindView(R.id.reset)
    Button reset;

    @OnClick(R.id.reset)
    public void backStep(){
        strokeView.hideHint();
        strokeView.pop();
    }

    @OnClick(R.id.tip)
    public void tip() {
        strokeView.showHint();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_test_home;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        toolbar.setTitle(getString(R.string.test_home));
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<Point> infos= new ArrayList<Point>();
        infos.add(new Point(3, 0,16));
        infos.add(new Point(1, 5,16));
        infos.add(new Point(6, 2,16));
        infos.add(new Point(0, 2,16));
        infos.add(new Point(5, 5,16));
        infos.add(new Point(3, 0,16));


        LinesInfo linesInfo=new LinesInfo(infos);
        strokeView.setLineInfo(linesInfo);
        strokeView.setListener(new OneStrokeView.OnFinishedLister() {
            @Override
            public void finished() {
                ToastUtil.ShortToast("OK");
            }
        });


    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
