package com.tap.tapcolorskit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static Intent start(final Activity activity) {
        return new Intent(activity, SettingsActivity.class);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.frame_layout);
        this.getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
        private Preference fontColorPref;
        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            this.addPreferencesFromResource(R.xml.preferences);

            fontColorPref =  this.findPreference("preference_color_key");
            fontColorPref.setOnPreferenceChangeListener(this);

            this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            updateSummary(fontColorPref);
        }

        @Override
        public boolean onPreferenceChange(final Preference preference, final Object newValue) {

            if (preference.equals(fontColorPref)) {
                return true;
            }
            return false;
        }

        @Override
        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {

            if (fontColorPref != null && key.equals(fontColorPref.getKey())) {
                final String value = sharedPreferences.getString(key, "");
                if(value!=null)
                    fontColorPref.setSummary(value);
            }
        }

        private void updateSummary(Preference fontColorPref){
            String summary = this.getPreferenceManager().getSharedPreferences().getString(fontColorPref.getKey(),"");
            fontColorPref.setSummary(summary);
        }
    }
}
