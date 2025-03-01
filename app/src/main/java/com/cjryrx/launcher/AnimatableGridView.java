package com.cjryrx.launcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

public class AnimatableGridView extends GridView {
    private int centerY;
    private float parallaxFactor = 0.2f;
    private boolean enableParallax = true;
    private int columnCount = 4;


    public AnimatableGridView(Context context) {
        super(context);
        init(context, null);
    }

    public AnimatableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimatableGridView);
        parallaxFactor = ta.getFloat(R.styleable.AnimatableGridView_parallaxFactor, 0.24f);
        enableParallax = ta.getBoolean(R.styleable.AnimatableGridView_enableParallax, true);
        ta.recycle();
        setupScrollListener();
        setNumColumns(columnCount);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void setupScrollListener() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                applyTransformation();
                View v = getChildAt(firstVisibleItem);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    applyTransformation();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerY = getHeight() / 2;
        applyTransformation(); // 尺寸变化时立即生效
    }

    private void applyTransformation() {
        if (!enableParallax || centerY == 0 || getChildCount() == 0) return;

        final float maxOffset = centerY;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == null) continue;
            int childCenterY = (child.getTop() + child.getBottom()) / 2;
            float offset = Math.abs(centerY - childCenterY);
            float ratio = Math.min(offset / maxOffset, 1f);
            // 视差平移
            float translationX = (float) -Math.pow(offset * ratio * parallaxFactor, parallaxFactor + 1f);
            float scale = 1f - 0.2f * ratio;

            child.setTranslationX(translationX + 30);
            child.setScaleX(scale);
            child.setScaleY(scale);;
        }
    }

    // 提供设置方法
    public void setParallaxFactor(float factor) {
        this.parallaxFactor = factor;
        applyTransformation();
    }

    public void setEnableParallax(boolean enable) {
        this.enableParallax = enable;
        applyTransformation();
    }

    public int getSpanCount() {
        return columnCount;
    }

    public void setSpanCount(int columnCount) {
        this.columnCount = columnCount;
    }


}