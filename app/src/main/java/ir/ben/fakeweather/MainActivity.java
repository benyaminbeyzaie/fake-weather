package ir.ben.fakeweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import ir.ben.fakeweather.enums.SelectedPage;
import ir.ben.fakeweather.fragments.Home;
import ir.ben.fakeweather.fragments.Setting;
import ir.ben.fakeweather.fragments.ShowWeather;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.view_models.UiStateViewModel;
import ir.ben.fakeweather.view_models.WeatherViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Setting setting = new Setting();
    Home home = new Home();
    SharedPreferences sharedpreferences;
    UiStateViewModel uiStateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiStateViewModel = new ViewModelProvider(this).get(UiStateViewModel.class);
//        WeatherViewModel weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

//        weatherViewModel.refresh(51.5072, 0.1276);

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


        if (uiStateViewModel.getSelectedPage().getValue() == null || uiStateViewModel.getSelectedPage().getValue() == SelectedPage.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, setting).commit();
        }

        Log.d("Main", "Set the select listener");

        bottomNavigationView.setOnItemSelectedListener(item -> {
//            Log.d("Main", "Clicked on item");
            System.out.println(item.getItemId());
            switch (item.getItemId()) {
                case R.id.setting:
                    uiStateViewModel.changePage(SelectedPage.setting);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, setting).commit();
                    if (uiStateViewModel.getSelectedPage().getValue() == null || !uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.setting)) {
                        uiStateViewModel.changePage(SelectedPage.setting);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        );
                        fragmentTransaction.replace(R.id.container, setting);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        Log.d("Main", "Clicked on setting");
                    }

//                    getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(
//                                    R.anim.slide_in,  // enter
//                                    R.anim.fade_out,  // exit
//                                    R.anim.fade_in,   // popEnter
//                                    R.anim.slide_out  // popExit
//                            ).replace(R.id.container, setting).addToBackStack(null).commit();
                    return true;
                case R.id.home:
                    uiStateViewModel.changePage(SelectedPage.home);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                    if (uiStateViewModel.getSelectedPage().getValue() != null && !uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.home)) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
//                        onBackPressed();
                        Log.d("Main", "Clicked on home");
                    }
//                    if (model.getLastSelectedPage() !=null && model.getSelectedPage().getValue().equals(SelectedPage.show_weather))
//                        model.changePage(SelectedPage.show_weather);
//                    else
//                        model.changePage(SelectedPage.home);
                    uiStateViewModel.changePage(SelectedPage.home);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                    return true;
            }
            return false;
        });

    }


    public void showMore(Daily daily, String time) {
        uiStateViewModel.changePage(SelectedPage.show_weather);
        ShowWeather showWeather = new ShowWeather(daily, time);
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                ).
                replace(R.id.container, showWeather).
                addToBackStack(null).
                commit();
    }


    @Override
    public void onBackPressed() {

        if(uiStateViewModel.getSelectedPage().getValue()!=null && uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.show_weather)){
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
            uiStateViewModel.changePage(SelectedPage.home);
            super.onBackPressed();
//            getSupportFragmentManager().popBackStack();
        }else if (uiStateViewModel.getSelectedPage().getValue()!=null && uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.setting)){
            uiStateViewModel.changePage(SelectedPage.home);
            super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }
}