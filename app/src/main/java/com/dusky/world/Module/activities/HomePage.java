package com.dusky.world.Module.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.dusky.duskyplayer.video.ADVideoPlayer;
import com.dusky.world.Adapter.TooSimpleAdapter;
import com.dusky.world.Adapter.WebBannerAdapter;
import com.dusky.world.Adapter.fateAdapter;
import com.dusky.world.Design.helper.CircleCropBorder;
import com.dusky.world.Design.helper.EndlessRecyclerOnScrollListener;
import com.dusky.world.Base.BaseActivity;
import com.dusky.world.Base.DuskyApp;
import com.dusky.world.Module.entity.TooSimple;
import com.dusky.world.R;
import com.dusky.world.Utils.CommonUtil;
import com.dusky.world.Utils.ToastUtil;
import com.nbsix.dsy.badgeView.BadgeView;
import com.nbsix.dsy.bannerview.BannerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class HomePage extends BaseActivity {
    @BindView(R.id.banner)
    BannerView bannerView;
    WebBannerAdapter webBannerAdapter;

    //@BindView(R.id.recyclerView)
    //RecyclerView recyclerView;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private boolean mIsRefreshing = false;
    private int pageNum = 1;
    private int pageSize = 10;

    @BindView(R.id.set)
    ImageView set;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.msg)
    ImageView msg;

    @BindView(R.id.user_avatar)
    ImageView user_avatar;

    @BindView(R.id.set_layout)
    RelativeLayout set_layout;
    @BindView(R.id.search_layout)
    RelativeLayout search_layout;
    @BindView(R.id.msg_layout)
    RelativeLayout msg_layout;

    @BindView(R.id.recyclerView_fate)
    RecyclerView recyclerView_fate;
    @BindView(R.id.recyclerView_tooSimple)
    RecyclerView recyclerView_tooSimple;

    @OnClick(R.id.set)
    public void goset(){
        ToastUtil.ShortToast("?");
    }
    @OnClick(R.id.search)
    public void gocup(){
        ToastUtil.ShortToast("?");
    }
    @OnClick(R.id.msg)
    public void gomsg(){
        ToastUtil.ShortToast("?");
    }

    public void setBadge(){
        new BadgeView(this)
                .bindTarget(msg_layout)
                .setBadgeNumber(99).setBadgeTextSize(6,true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.green));
        new BadgeView(this)
                .bindTarget(search_layout)
                .setBadgeNumber(5).setBadgeTextSize(6,true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red));
        new BadgeView(this)
                .bindTarget(set_layout).setBadgeText("new!").setBadgeTextSize(6,true)
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.orange));

    }


    public void setFate(){
        fateAdapter fateAdapter=new fateAdapter(HomePage.this);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 3){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight());
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }

        };
        recyclerView_fate.setLayoutManager(layoutManage);
        recyclerView_fate.setAdapter(fateAdapter);
        recyclerView_fate.setNestedScrollingEnabled(false);

    }

    private int setSpanSize(int position, List<TooSimple> tooSimples) {

        return tooSimples.get(position).getSpanCount();
    }

    public void setTooSimple(){
        TooSimpleAdapter tooSimpleAdapter=new TooSimpleAdapter(HomePage.this);

        GridLayoutManager layoutManage = new GridLayoutManager(this, 2){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight());
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }

        };
        layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position,tooSimpleAdapter.getList());
            }
        });



        recyclerView_tooSimple.setLayoutManager(layoutManage);
        recyclerView_tooSimple.setAdapter(tooSimpleAdapter);
        recyclerView_tooSimple.setNestedScrollingEnabled(false);

    }


    @SuppressLint("CheckResult")
    public void loadData(){
        MultiTransformation multi = new MultiTransformation(new CircleCropBorder(4,getResources().getColor(R.color.gray_light)));
        Glide.with(HomePage.this).load(R.drawable.banner).apply(bitmapTransform(multi)).into(user_avatar);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_homepage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showNoNetWorkDlg(this);
        }

        setBadge();
        loadData();
        setFate();
        setTooSimple();
        List<String> list = new ArrayList<>();
        list.add("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=3184221534,2238244948&fm=27&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1794621527,1964098559&fm=27&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg");
        webBannerAdapter=new WebBannerAdapter(this,null);
        webBannerAdapter.setOnBannerItemClickListener(new BannerView.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position,int type) {

                switch (type){
                    case WebBannerAdapter.TYPE_IMG:
                        Toast.makeText(HomePage.this, "点击了第  " + position+"  项,"+"类型是图片banner", Toast.LENGTH_SHORT).show();
                        break;
                    case WebBannerAdapter.TYPE_MUSIC:
                        Toast.makeText(HomePage.this, "点击了第  " + position+"  项,"+"类型是音乐banner", Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(HomePage.this,MusicActivity.class);
                        startActivity(it);
                        break;
                    case WebBannerAdapter.TYPE_VIDEO:
                        Toast.makeText(HomePage.this, "点击了第  " + position+"  项,"+"类型是视频banner", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        bannerView.setAdapter(webBannerAdapter);
    }

    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                DuskyApp.mInstance.Exit();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
