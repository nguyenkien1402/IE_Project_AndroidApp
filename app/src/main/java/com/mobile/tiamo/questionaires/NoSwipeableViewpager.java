package com.mobile.tiamo.questionaires;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoSwipeableViewpager extends ViewPager {

    private boolean enabled;
    public NoSwipeableViewpager(@NonNull Context context) {
        super(context);
    }

    public NoSwipeableViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !enabled && super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return !enabled && super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !enabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return !enabled && super.canScrollHorizontally(direction);
    }

    public boolean getSwipeLocked() {
        return enabled;
    }

    public void setSwipeLocked(boolean enabled) {
        this.enabled = enabled;
    }
}
