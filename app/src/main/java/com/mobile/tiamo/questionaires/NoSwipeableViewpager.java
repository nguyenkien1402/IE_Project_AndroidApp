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
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if(this.enabled){
            return super.onInterceptHoverEvent(event);
        }
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
