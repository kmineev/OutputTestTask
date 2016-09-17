package com.example.kostez.outputtesttask.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.kostez.outputtesttask.helpers.LocaleHelper;
import com.example.kostez.outputtesttask.R;

import static com.example.kostez.outputtesttask.applications.MyApplication.getPreferences;

/**
 * Created by Kostez on 10.09.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    private ListPreference listPreference;

    public static final String LANGUAGE = "language";
    public static final String LANGUAGE_IS_CHANGED = "language_is_changed";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        listPreference = (ListPreference) getPreferenceManager().findPreference(LANGUAGE);
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                LocaleHelper.setLocale(getActivity(), o.toString());

                SharedPreferences.Editor editor = getPreferences().edit();
                editor.putBoolean(LANGUAGE_IS_CHANGED, true);
                editor.commit();

                updateViews();
                return true;
            }
        });
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateViews() {
        restartActivity();
    }
}