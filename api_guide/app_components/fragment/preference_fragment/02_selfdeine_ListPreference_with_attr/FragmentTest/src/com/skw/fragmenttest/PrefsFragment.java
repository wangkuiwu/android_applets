package com.skw.fragmenttest;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

public class PrefsFragment extends PreferenceFragment 
    implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String TAG = "##PrefsFragment##";

    private static final String ICON_PREFERENCE     = "icon_list_preference";
    private static final String LIST_PREFERENCE     = "list_preference";

    private IconListPreference mIconPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        mIconPref = (IconListPreference) findPreference(ICON_PREFERENCE);
        mIconPref.setOnPreferenceClickListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Set summary to be the user-description for the selected value
        Preference pref = findPreference(key);
        if (key.equals(ICON_PREFERENCE)) {
            String value = sharedPreferences.getString(key, "");
            //pref.setSummary(value);
            Log.d(TAG, "List: value="+value);
        } else if (key.equals(LIST_PREFERENCE)) {
            String value = sharedPreferences.getString(key, "");
            //pref.setSummary(value);
            Log.d(TAG, "List: value="+value);
        } 
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        SharedPreferences sharedPreferences = preference.getSharedPreferences();
        String value = sharedPreferences.getString(preference.getKey(), "");
        Log.d(TAG, "onPreferenceClick: value="+value);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
