package samples.exoguru.bloodlink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Map;

public class SettingsActivity extends PreferenceActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.commit();


    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Set summary to be the user-description for the selected value
            Preference preference = findPreference(key);

            if (preference instanceof ListPreference || preference instanceof EditTextPreference) {
                preference.setSummary(sharedPreferences.getString(key, preference.toString()));
            }
        }

        @Override
        public void onResume() {
            super.onResume();

            SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

            Map<String, ?> keys = preferences.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {

                Preference preference = findPreference(entry.getKey());

                //Log.e("onResume", "preference key is " + entry.getKey());
                //Log.e("onResume", "preference is " + preference);
                if (preference instanceof ListPreference) {
                    preference.setSummary(((ListPreference) preference).getEntry());
                }
                if (preference instanceof EditTextPreference
                        && !preference.getKey().equals("lastDonationDateInMillis")) {
                    preference.setSummary(((EditTextPreference) preference).getText());
                }

                if (entry.getKey().equals("lastDonationDateInMillis")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    preference.setSummary(formatter.format(Long.parseLong(((EditTextPreference) preference).getText())));
                }

            }
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }


    }

}




