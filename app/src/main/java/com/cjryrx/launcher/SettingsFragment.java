package com.cjryrx.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Preference p = findPreference("restart");
        if(p == null) Toast.makeText(getContext(), "Error Occurred. Can't find 'restart'.", Toast.LENGTH_LONG).show();
        else{
            p.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent("con.cjryrx.launcher.RESTART");
                Context c = getContext();
                if(c!= null) c.sendBroadcast(intent);
                else Toast.makeText(getContext(), "Error Occurred. Can't restart.", Toast.LENGTH_LONG).show();
                return true;
            });
        }
    }
}
