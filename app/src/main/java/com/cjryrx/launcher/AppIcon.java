package com.cjryrx.launcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.Objects;

public class AppIcon extends androidx.appcompat.widget.AppCompatImageView {

    ResolveInfo res;

    public boolean isEmpty;

    public AppIcon(Context context) {
        super(context);
        this.res = null;
        isEmpty = true;
        init();
    }

    public AppIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.res = null;
        isEmpty = true;
        init();
    }

    private void init(){
        if(!isEmpty){
            if(res == null){
                setImageDrawable(AppCompatResources.getDrawable(getContext(), R.mipmap.bg));
            }else {
                Drawable d = res.loadIcon(getContext().getPackageManager());
                setImageDrawable(d);
            }
        }
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int side = Math.min(view.getWidth(), view.getHeight());
                outline.setOval(new Rect(0, (view.getHeight() - side) / 2, side, (view.getHeight() + side) / 2));
            }
        });
        setClipToOutline(true);
        setOnClickListener(v -> {
            String pkg = res.activityInfo.packageName;
            ComponentName c;
            if(Objects.equals(pkg, getContext().getPackageName())){
                c = new ComponentName(pkg, getContext().getPackageName() + ".SettingsActivity");
            } else {
                c = new ComponentName(pkg, res.activityInfo.name);
            }

            Intent i = new Intent();
            i.setComponent(c);
            getContext().startActivity(i);
        });
    }

    public void setRes(ResolveInfo to){
        res = to;
        isEmpty = false;
        init();
    }
}
