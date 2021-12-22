package com.example.earthquakeapplication;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class EarthQuakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        /**
         * This method called when the settings activity is created
         * @param savedInstanceState
         */
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference orderBy =    findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        /**
         * This method is called whenever the user changes the app preferences
         * @param preference    Preference object
         * @param value         Value passed to the Preference Object
         * @return              Boolean value to notify method was called
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object value){
            String stringValue = value.toString();
            if(preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int preIndex = listPreference.findIndexOfValue(stringValue);
                if(preIndex >= 0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preIndex]);
                }
            }
            else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Helper method to set the summary of the Preference Object
         * @param preference    Prefernce  Ojbject
         */
        private void bindPreferenceSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}