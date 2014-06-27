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

    private static final String CHECK_PREFERENCE    = "checkbox_preference";
    private static final String EDITTEXT_PREFERENCE = "edittext_preference";
    private static final String LIST_PREFERENCE     = "list_preference";
    private static final String SWITCH_PREFERENCE   = "switch_preferece";
    private static final String SEEKBAR_PREFERENCE  = "seekbar_preference";

    private Preference mEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        mEditText = (Preference) findPreference(EDITTEXT_PREFERENCE);
        mEditText.setOnPreferenceClickListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Set summary to be the user-description for the selected value
        Preference connectionPref = findPreference(key);
        if (key.equals(CHECK_PREFERENCE)) {
            boolean checked = sharedPreferences.getBoolean(key, false);
            Log.d(TAG, "CheckBox: checked="+checked);
        } else if (key.equals(EDITTEXT_PREFERENCE)) {
            String value = sharedPreferences.getString(key, "");
            connectionPref.setSummary(value);
            Log.d(TAG, "EditText: value="+value);
        } else if (key.equals(LIST_PREFERENCE)) {
            String value = sharedPreferences.getString(key, "");
            connectionPref.setSummary(value);
            Log.d(TAG, "List: value="+value);
        } else if (key.equals(SWITCH_PREFERENCE)) {
            boolean checked = sharedPreferences.getBoolean(key, false);
            Log.d(TAG, "Switch: checked="+checked);
        } else if (key.equals(SEEKBAR_PREFERENCE)) {
            int value = sharedPreferences.getInt(key, 0);
            Log.d(TAG, "Seekbar: value="+value);
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
