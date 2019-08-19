package com.dusky.world.Design.helper;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/9/29
 * @DESCRIPTION:顶部显示，上拉隐藏（回想下当时是想做回到顶部）
 */
public class FooterBehavior extends CoordinatorLayout.Behavior<View> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final String TAG = FooterBehavior.class.getSimpleName();
    private static final int ANIMATION_DURATION = 600;//动画执行时长

    public FooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                                       View directTargetChild, View target, int nestedScrollAxes, int type) {
        if ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            return true;//是垂直滑动，那么返回true通知后面的方法继续处理
        }else{
            return false;
        }
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        if (reachTop(target)&&dy<0) {
            show(child);  //向下滑动，并且到顶了，显示view
        } else {
            hide(child); //向上滑动，隐藏view
        }
    }

    //对两种情况的判断
    private boolean reachTop(View target) {
        if (target instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) target;
            int scrollY = nestedScrollView.getScrollY();
            if (scrollY==0) {
                return true;
            }
        } else if (target instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) target;
            if (!recyclerView.canScrollVertically(-1)) {
                return true;
            }
        }
        return false;
    }
    private boolean reachBottom(View target) {
        if (target instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) target;
            int scrollY = nestedScrollView.getScrollY();
            View onlyChild = nestedScrollView.getChildAt(0);
            if (onlyChild.getHeight() <= scrollY + nestedScrollView.getHeight()) {
                return true;
            }
        } else if (target instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) target;
            if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                    >= recyclerView.computeVerticalScrollRange()) {
                return true;
            }
        }
        return false;
    }

    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(view.getHeight()).
                setInterpolator(INTERPOLATOR).setDuration(ANIMATION_DURATION);
        animator.start();
    }

    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(0).
                setInterpolator(INTERPOLATOR).
                setDuration(ANIMATION_DURATION);
        animator.start();

    }
}