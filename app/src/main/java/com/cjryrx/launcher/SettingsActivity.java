package com.cjryrx.launcher;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsFragment sf = new SettingsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!sf.isAdded()) ft.add(R.id.settings_container, sf);
        ft.commit();
        setContentView(R.layout.container_settings);
    }
}