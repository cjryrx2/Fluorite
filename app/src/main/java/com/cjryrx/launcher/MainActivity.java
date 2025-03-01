package com.cjryrx.launcher;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.palette.graphics.Palette;
import androidx.preference.PreferenceManager;

import com.cjryrx.launcher.databinding.ContainerBinding;
import com.cjryrx.launcher.databinding.Main1Binding;
import com.cjryrx.launcher.databinding.Main2Binding;
import com.cjryrx.launcher.databinding.Main3Binding;
import com.cjryrx.launcher.databinding.MainAnalogBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Main1Binding main1;
    Main2Binding main2;
    Main3Binding main3;
    MainAnalogBinding main5;

    ContainerBinding containerBinding;

    private List<ResolveInfo> apps;

    View mainPage;

    DisplayMetrics dm; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = new DisplayMetrics();
        main1 = Main1Binding.inflate(getLayoutInflater());
        main2 = Main2Binding.inflate(getLayoutInflater());
        main3 = Main3Binding.inflate(getLayoutInflater());
        main5 = MainAnalogBinding.inflate(getLayoutInflater());
        containerBinding = ContainerBinding.inflate(getLayoutInflater());
        String[] perms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE};
        }else {
            perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        requestPermissions(perms, 2222);
        WallpaperManager wpm = WallpaperManager.getInstance(this);
        Drawable wallpaper = wpm.getDrawable();
        if(wallpaper == null){
            wallpaper = AppCompatResources.getDrawable(this, R.mipmap.bg);
        }
        containerBinding.container.setBackground(wallpaper);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sp.getString("mainpage_selection", "0")){
            default:
                mainPage = main1.getRoot();
                if(sp.getBoolean("use_dynamic_color", false)){
                    assert wallpaper != null;
                    Palette p = Palette.from(((BitmapDrawable) wallpaper).getBitmap()).generate();
                    main1.clock.setTextColor(p.getVibrantColor(Color.WHITE));
                    main1.date.setTextColor(p.getMutedColor(Color.YELLOW));
                }
                break;
            case "2":
                mainPage = main2.getRoot();
                if(sp.getBoolean("use_dynamic_color", false)){
                    assert wallpaper != null;
                    Palette p = Palette.from(((BitmapDrawable) wallpaper).getBitmap()).generate();
                    main2.clock.setTextColor(p.getVibrantColor(Color.WHITE));
                    main2.date.setTextColor(p.getMutedColor(Color.YELLOW));
                }
                break;
            case "3":
                mainPage = main3.getRoot();
                if(sp.getBoolean("use_dynamic_color", false)){
                    assert wallpaper != null;
                    Palette p = Palette.from(((BitmapDrawable) wallpaper).getBitmap()).generate();
                    main3.clock.setTextColor(p.getVibrantColor(Color.WHITE));
                    main3.date.setTextColor(p.getMutedColor(Color.YELLOW));
                }
                break;
            case "4":
                mainPage = main5.getRoot();
                if(sp.getBoolean("use_dynamic_color", false)){
                    assert wallpaper != null;
                    Palette p = Palette.from(((BitmapDrawable) wallpaper).getBitmap()).generate();
                    main5.analogClock.setSecondColor(p.getVibrantColor(Color.WHITE));
                    main5.analogClock.setHourHandColor(p.getMutedColor(Color.YELLOW));
                    main5.analogClock.setMinuteHandColor(p.getDominantColor(Color.GRAY));
                }
                break;
        }
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        apps = getPackageManager().queryIntentActivities(i, 0);
        AnimatableGridView gv = new AnimatableGridView(this);
        gv.setAdapter(new AppsAdapter());
        gv.setNestedScrollingEnabled(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gv.setLayoutParams(lp);
        gv.setEnableParallax(true);
        gv.setScrollContainer(false);
        gv.setScrollBarSize(0);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(lp);
        ll.addView(gv);
        ll.setClipChildren(false);
        ll.setClipToPadding(false);
        ll.setPadding(50, 10, 10, 20);
        ll.setScrollContainer(true);

        containerBinding.container.setInAnimation(this, R.anim.anim_in);
        containerBinding.container.setOutAnimation(this, R.anim.anim_out);
        containerBinding.container.addView(mainPage, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        containerBinding.container.addView(ll, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        Drawable finalWallpaper = wallpaper;
        containerBinding.container.setOnLongClickListener(v -> {
            if(containerBinding.container.getCurrentView() == mainPage){
                containerBinding.container.showNext();
                BitmapDrawable bg = (BitmapDrawable) containerBinding.container.getBackground();
                containerBinding.container.setBackground(Blur.BoxBlurFilter(bg.getBitmap()));
            }else {
                containerBinding.container.showPrevious();
                containerBinding.container.setBackground(finalWallpaper == null ? AppCompatResources.getDrawable(this, R.mipmap.bg) : finalWallpaper);
            }
            return false;
        });
        setContentView(containerBinding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sp.getString("mainpage_selection", "0")){
            default:
                mainPage = main1.getRoot();
                break;
            case "2":
                mainPage = main2.getRoot();
                break;
            case "3":
                mainPage = main3.getRoot();
                break;
            case "4":
                mainPage = main5.getRoot();
                break;
        }
        mainPage.invalidate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "有权限未提供，可能会导致一些问题。", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
    class AppsAdapter extends BaseAdapter {

        public AppsAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return apps.size();
        }

        @Override
        public Object getItem(int position) {
            return apps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new AppIcon(getApplicationContext());
                ((AppIcon) convertView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                ((AppIcon) convertView).setAdjustViewBounds(true);
            }
            ((AppIcon) convertView).setRes(apps.get(position));
            return convertView;
        }
    }
}