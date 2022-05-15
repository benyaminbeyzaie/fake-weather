package ir.ben.fakeweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.ben.fakeweather.enums.SelectedPage;
import ir.ben.fakeweather.fragments.Home;
import ir.ben.fakeweather.fragments.Setting;
import ir.ben.fakeweather.fragments.ShowWeather;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.view_models.UiStateViewModel;

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
            Log.d("Main", "Clicked on item");
            switch (item.getItemId()) {
                case R.id.setting:
                    if (uiStateViewModel.getSelectedPage().getValue() != null && uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.show_weather)) {
                        onBackPressed();
                        uiStateViewModel.changePage(SelectedPage.setting);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        );
                        fragmentTransaction.replace(R.id.container, setting);
                        fragmentTransaction.addToBackStack("null1");
                        fragmentTransaction.commit();
                        Log.d("Main", "Clicked on setting");
                    }
                    else if (uiStateViewModel.getSelectedPage().getValue() == null || !uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.setting)) {
                        uiStateViewModel.changePage(SelectedPage.setting);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        );
                        fragmentTransaction.replace(R.id.container, setting);
                        fragmentTransaction.addToBackStack("null1");
                        fragmentTransaction.commit();
                        Log.d("Main", "Clicked on setting");
                    }



                    return true;
                case R.id.home:
                    if (uiStateViewModel.getSelectedPage().getValue() != null && !uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.home)) {
                        onBackPressed();
                        Log.d("Main", "Clicked on home");
                    }
                    uiStateViewModel.changePage(SelectedPage.home);
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
                addToBackStack("null2").
                commit();
    }


    @Override
    public void onBackPressed() {
        Log.d("Main" , uiStateViewModel.getSelectedPage().getValue()+"");
//        super.onBackPressed();
        if(uiStateViewModel.getSelectedPage().getValue()!=null && uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.show_weather)){
            uiStateViewModel.changePage(SelectedPage.home);
            super.onBackPressed();
        }else if (uiStateViewModel.getSelectedPage().getValue()!=null && uiStateViewModel.getSelectedPage().getValue().equals(SelectedPage.setting)){
            uiStateViewModel.changePage(SelectedPage.home);
            super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}