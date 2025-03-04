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

    private String animType = "flow";

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
        animType = ta.getString(R.styleable.AnimatableGridView_animationType);
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

        switch (animType){
            case "none":
                return;
            default:
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child == null) continue;
                    int childCenterY = (child.getTop() + child.getBottom()) / 2;
                    float offset = Math.abs(centerY - childCenterY);
                    float ratio = Math.min(offset / maxOffset, 1f);
                    float translationX = (float) -Math.pow(offset * ratio * parallaxFactor, parallaxFactor + 1f);
                    float scale = 1f - 0.2f * ratio;
                    child.setTranslationX(translationX + 60);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
                break;
            case "drizzle":
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child == null) continue;
                    int childCenterY = (child.getTop() + child.getBottom()) / 2;
                    float offset = Math.abs(centerY - childCenterY);
                    float ratio = Math.min(offset / maxOffset, 1f);
                    float translationX = (float) (-Math.cos(ratio * 8) / Math.pow(parallaxFactor, 2));
                    float scale = 1f - 0.2f * ratio;
                    child.setTranslationX(translationX);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
                break;
            case "fly":
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child == null) continue;
                    int childCenterY = (child.getTop() + child.getBottom()) / 2;
                    float offset = Math.abs(centerY - childCenterY);
                    float ratio = Math.min(offset / maxOffset, 1f);
                    float translationX = (float) offset * parallaxFactor * 2;
                    float scale = 1f - 0.2f * ratio;
                    child.setTranslationX(translationX);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
                break;
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


    public String getAnimType() {
        return animType;
    }

    public void setAnimType(String animType) {
        this.animType = animType;
    }
}