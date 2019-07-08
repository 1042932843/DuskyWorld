package com.nbsix.dsy.bannerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/26
 * @DESCRIPTION:
 */
public class BannerView extends FrameLayout{

    private boolean indicatorDisplayed;//指示器是否显示
    private int indicatorMargin;//指示器间距
    private int currentIndex;//指示器当前位置
    private int orientation;//指示器方向（横排还是竖排），这个方向跟轮播是一样的。
    private boolean IsAuto;//是否自动轮播

    public boolean isChanging() {
        return isChanging;
    }


    private int AUTO_PLAY = 0x1;

    private boolean isChanging = false;
    private int interval;//当激活自动轮播时设置的时间间隔
    private int itemSpacing;//轮播单位的间距
    private float centerScale;//缩放大小
    private float slideSpeed;//滑动变化切换速度

    private int bannerSize;//轮播数据数量

    private RecyclerView indicatorContainer;//用来做指示器的RecyclerView
    private RecyclerView bannerContainer;//用来做轮播图的RecyclerView
    private BannerLayoutManager bannerLayoutManager;//定制轮播的LayoutManager

    private IndicatorAdapter indicatorAdapter;

    private Drawable mSelectedDrawable;//指示器选中
    private Drawable mUnselectedDrawable;//指示器未选中

    //第一属于程序内实例化时采用，之传入Context即可
    public BannerView(Context context) {
        super(context);
    }
    //第二个用于layout文件实例化，会把XML内的参数通过AttributeSet带入到View内。
    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
        initIndicator(context);
        initBanner(context);
    }
    //第三个主题的style信息，也会从XML里带入
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //新的，用不上。
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    //在这里进行初始化
    private void init(Context context, AttributeSet attrs){
        //TypedArray是存储资源数组的容器，他可以通过obtaiStyledAttributes()方法创建出来。不过创建完后，如果不在使用了，请注意调用recycle()方法把它释放。
        //他可以通过检索res资源中结构的特定值的索引的到对应的资源。所以换句话说，通过他能够得到我们想要的资源。
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        indicatorDisplayed=typedArray.getBoolean(R.styleable.BannerView_BannerView_indicatorDisplayed, true);//指示器显示状态获取，默认显示
        indicatorMargin=typedArray.getDimensionPixelSize(R.styleable.BannerView_BannerView_indicatorMargin, 4);//指示器显示状态获取，默认显示
        IsAuto=typedArray.getBoolean(R.styleable.BannerView_BannerView_auto, false);//是否自动轮播，默认不轮播
        interval=typedArray.getInt(R.styleable.BannerView_BannerView_interval, 5000);//时间间隔获取,默认为5000
        itemSpacing=typedArray.getDimensionPixelSize(R.styleable.BannerView_BannerView_spacing, 20);//Item间距,默认为20
        centerScale=typedArray.getFloat(R.styleable.BannerView_BannerView_centerScale, 1.2f);//缩放大小,默认为1.2
        slideSpeed=typedArray.getFloat(R.styleable.BannerView_BannerView_slideSpeed, 1.0f);//时间间隔获取,默认为1
        orientation=typedArray.getInt(R.styleable.BannerView_BannerView_orientation, 0);//默认是横向的
        typedArray.recycle();
    }

    //初始化指示器
    private void initIndicator(Context context){
        //指示器部分

        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
            selectedGradientDrawable.setColor(Color.RED);
            selectedGradientDrawable.setSize(8, 8);
            selectedGradientDrawable.setCornerRadius(4);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(Color.GRAY);
            unSelectedGradientDrawable.setSize(8, 8);
            unSelectedGradientDrawable.setCornerRadius(4);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        indicatorContainer = new RecyclerView(context);
        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        indicatorContainer.setLayoutManager(indicatorLayoutManager);
        indicatorAdapter = new IndicatorAdapter();
        indicatorContainer.setAdapter(indicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER| Gravity.BOTTOM;
        int margin=12;
        params.setMargins(margin, margin, margin, margin);
        addView(indicatorContainer, params);
        if (!indicatorDisplayed) {
            indicatorContainer.setVisibility(GONE);
        }

    }

    //初始化轮播图
    private void initBanner(Context context){
        //轮播图部分
        bannerContainer = new RecyclerView(context);
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(bannerContainer, vpLayoutParams);
        bannerLayoutManager = new BannerLayoutManager(getContext(), orientation);
        bannerLayoutManager.setItemSpace(itemSpacing);
        bannerLayoutManager.setCenterScale(centerScale);
        bannerLayoutManager.setMoveSpeed(slideSpeed);
        bannerContainer.setLayoutManager(bannerLayoutManager);
        new CenterSnapHelper().attachToRecyclerView(bannerContainer);

        //对变化状态进行监听，并改变指示器
        bannerContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx != 0) {
                    isChanging=false;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int Current = bannerLayoutManager.getCurrentPosition();
                Log.d("xxx", "onScrollStateChanged");
                if (currentIndex != Current) {
                    currentIndex = Current;
                }
                if (newState == SCROLL_STATE_IDLE) {
                    isChanging=true;
                }
                refreshIndicator();

            }
        });
    }


    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AUTO_PLAY) {
                if (currentIndex == bannerLayoutManager.getCurrentPosition()) {
                    ++currentIndex;
                    bannerContainer.smoothScrollToPosition(currentIndex);
                    mHandler.sendEmptyMessageDelayed(AUTO_PLAY, interval);
                    refreshIndicator();
                }
            }
            return false;
        }
    });


    /**
     * 设置轮播间隔时间
     * @param interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 改变导航的指示点
     */
    protected void refreshIndicator() {
        if (indicatorDisplayed && bannerSize > 1) {
            indicatorAdapter.setPosition(currentIndex % bannerSize);
            indicatorAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 设置是否自动播放（上锁）
     *
     * @param start 开始播放
     */
    protected synchronized void setPlaying(boolean start) {
        if (IsAuto) {
            if (!isChanging && start) {
                mHandler.sendEmptyMessageDelayed(AUTO_PLAY, interval);
                isChanging=true;
            } else if (isChanging && !start) {
                mHandler.removeMessages(AUTO_PLAY);
                isChanging=false;
            }
        }
    }

    /**
     * 设置轮播数据集
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)bannerContainer.getItemAnimator()).setSupportsChangeAnimations(false);
        bannerContainer.setAdapter(adapter);
        bannerSize = adapter.getItemCount();
        bannerLayoutManager.setInfinite(bannerSize >= 3);//设置条件
    }


    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return bannerSize;
        }
    }

    // 监听事件
    public interface OnBannerItemClickListener {
        void onItemClick(int position,int type);
    }
}



