package com.sst.anouncements;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by eternitysst on 6/9/13.
 */
public class SettingsFragment extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    ListPreference list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("pref_key_refresh")) {
            Preference connectionPref = findPreference("pref_key_refresh");
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString("pref_key_refresh", ""));
            Intent tent = new Intent("com.sst.anouncements.STARTUPDATE");
            this.sendBroadcast(tent);

        }
    }
}
