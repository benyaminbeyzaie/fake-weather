package ir.ben.fakeweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import ir.ben.fakeweather.enums.SelectedPage;
import ir.ben.fakeweather.fragments.Home;
import ir.ben.fakeweather.fragments.Setting;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.view_models.UiStateViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Setting setting = new Setting();
    Home home = new Home();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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