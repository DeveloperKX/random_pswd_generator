package developerkx.random_pswd_generator;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.TwoStatePreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    int countOfEnabledSwitches = 0;
    Toast cantTurnOffToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        cantTurnOffToast = Toast.makeText(this.getApplicationContext(), R.string.toast_message_cant_turn_off_all, LENGTH_SHORT);

        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new GeneralPreferenceFragment()).commit();

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        boolean useLowercaseLetters = defaultSharedPreferences.getBoolean("lowercase_letters_switch", true);
        boolean useUppercaseLetters = defaultSharedPreferences.getBoolean("uppercase_letters_switch", true);
        boolean useDigits = defaultSharedPreferences.getBoolean("digits_switch", true);
        boolean useSpecialSymbols = defaultSharedPreferences.getBoolean("special_symbols_switch", true);

        if (useLowercaseLetters) {
            countOfEnabledSwitches += 1;
        }

        if (useUppercaseLetters) {
            countOfEnabledSwitches += 1;
        }

        if (useDigits) {
            countOfEnabledSwitches += 1;
        }

        if (useSpecialSymbols) {
            countOfEnabledSwitches += 1;
        }

        defaultSharedPreferences
            .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    boolean value1 = sharedPreferences.getBoolean(key, true);
                    boolean value2 = sharedPreferences.getBoolean(key, false);

                    if (value1 == value2) {
                        countOfEnabledSwitches += (value1 ? 1 : -1);
                    }

                    if (countOfEnabledSwitches <= 0 || value1 != value2) {
                        PreferenceFragment preferenceFragment = (PreferenceFragment) getFragmentManager().findFragmentById(android.R.id.content);
                        TwoStatePreference twoStatePreference = (TwoStatePreference) preferenceFragment.findPreference(key);
                        twoStatePreference.setChecked(true);

                        cantTurnOffToast.show();
                    }
                }
            });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
//            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
