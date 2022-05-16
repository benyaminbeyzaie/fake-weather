package ir.ben.fakeweather.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ir.ben.fakeweather.R;

public class Setting extends Fragment {

    public Setting() {
        // Required empty public constructor
    }

    SwitchMaterial themeSwitch;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        themeSwitch = getView().findViewById(R.id.theme_switch);

        Context context = getActivity();
        assert context != null;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        if (sharedPref.getBoolean(getString(R.string.is_dark), false)) {
            themeSwitch.setChecked(true);
        }else {
            themeSwitch.setChecked(false);
        }
        themeSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            final SharedPreferences.Editor editor = sharedPref.edit();
            if (isChecked) {
                editor.putBoolean(getString(R.string.is_dark), true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                editor.putBoolean(getString(R.string.is_dark), false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            editor.apply();
        });

    }
}