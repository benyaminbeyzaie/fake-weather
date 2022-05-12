package ir.ben.fakeweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.enums.SelectedPage;
import ir.ben.fakeweather.fragments.Home;
import ir.ben.fakeweather.fragments.Setting;
import ir.ben.fakeweather.view_models.UiStateViewModel;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Setting setting = new Setting();
    Home home;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        home = new Home();

        UiStateViewModel model = new ViewModelProvider(this).get(UiStateViewModel.class);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean isDark = sharedpreferences.getBoolean(getString(R.string.is_dark), false);

        Log.d("Main", "it is");

        setContentView(R.layout.activity_main);
        if (isDark) {
            Log.d("Main", "it is dark");

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        Log.d("Main", "on create");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);


        if (model.getSelectedPage().getValue() == null || model.getSelectedPage().getValue() == SelectedPage.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, setting).commit();
        }

        Log.d("Main", "Set the select listener");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("Main", "Clicked on item");
            System.out.println(item.getItemId());
            switch (item.getItemId()) {
                case R.id.setting:
                    model.changePage(SelectedPage.setting);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, setting).commit();
                    return true;
                case R.id.home:
                    model.changePage(SelectedPage.home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                    return true;
            }
            return false;
        });

    }

}