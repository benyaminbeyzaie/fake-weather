package ir.ben.fakeweather.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import ir.ben.fakeweather.MainActivity;
import ir.ben.fakeweather.R;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.utils.Functions;
import ir.ben.fakeweather.utils.WeatherAdaptor;
import ir.ben.fakeweather.view_models.WeatherViewModel;


public class Home extends Fragment {


    EditText cityName, latEdit, lonEdit;
    Button searchButton;
    RadioGroup radio;
    RecyclerView recyclerView;
    WeatherAdaptor weatherAdaptor;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    boolean isByCity = false;
    CardView currentState;
    CardView currentWeather;

    TextView currentTime, currentLocation, currentTemp, currentHumidity, currentPresure, currentWindSpeed, currentWindDirection;
    ImageView currentImage;
    TextView currentStateView;
    ImageButton currentExpand;

    private WeatherViewModel weatherViewModel;
    LiveData<OpenWeatherMap> openWeatherMapLiveData;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning, isSent=false;
    private static final long START_TIME_IN_MILLIS = 5000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private String cityNameString , LatString , LonString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPref = getContext().getSharedPreferences("Weather", MODE_PRIVATE);
        editor = sharedPref.edit();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        openWeatherMapLiveData = weatherViewModel.getOpenWeatherMapLiveData();




        currentTime = view.findViewById(R.id.time_id_current);
        currentLocation = view.findViewById(R.id.location_id_current);
        currentTemp = view.findViewById(R.id.temp_id_current);
        currentHumidity = view.findViewById(R.id.humidity_id_current);
        currentPresure = view.findViewById(R.id.pressure_id_current);
        currentWindSpeed = view.findViewById(R.id.wind_id_current);
        currentWindDirection = view.findViewById(R.id.wind_dir_id_current);
        currentImage = view.findViewById(R.id.imageView_current);
        currentStateView = view.findViewById(R.id.status_id_current);


        radio = (RadioGroup) view.findViewById(R.id.radio_button);

        cityName = view.findViewById(R.id.city_name);
        latEdit = view.findViewById(R.id.lat);
        lonEdit = view.findViewById(R.id.lon);

        searchButton = view.findViewById(R.id.search_button);

        radio.setOnCheckedChangeListener(this::radioAction);


        currentState = view.findViewById(R.id.current_card_view);
        currentWeather = view.findViewById(R.id.cardView_current);
        currentExpand = view.findViewById(R.id.current_expandable);

        boolean isHide = sharedPref.getBoolean(getString(R.string.is_hide), false);

        Log.d("Main", "it is");
        if (isHide) {
            currentWeather.setVisibility(View.GONE);
        }
        Log.d("Main", "on create");


        currentExpand.setOnClickListener(view1 -> {
            expandAction();
        });


        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        weatherAdaptor = new WeatherAdaptor();
        recyclerView.setAdapter(weatherAdaptor);

        searchButton.setOnClickListener(v -> {
            if (!isSent) {
//                stopTimer();
                searchAction();
                isSent = true;
            }else {
                g(getContext());

            }
        });

        runChangeListeners();

        return view;
    }

    private void g(Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle("Title")
                .setMessage(R.string.confirm_massege)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopTimer();
                        searchAction();
                        isSent = true;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void runChangeListeners() {
        cityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable==null || editable.toString().equals("")){
                    stopTimer();
                }else {
                    stopTimer();
                    runCityTimer();
                    isSent = false;
                }
            }
        });


        latEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable==null || editable.toString().equals("") || lonEdit.getText() == null || lonEdit.getText().toString().equals("")){
                    stopTimer();
                }else {
                    stopTimer();
                    locationTimer();
                    isSent = false;
                }
            }
        });

        lonEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable==null || editable.toString().equals("") || latEdit.getText() == null || latEdit.getText().toString().equals("")){
                    stopTimer();
                }else {
                    stopTimer();
                    locationTimer();
                    isSent = false;
                }
            }
        });

    }

    public void runCityTimer(){
        Log.d("Timer" , "City Timer has started.");
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis , 500) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                Log.d("Timer" , "time left: " + mTimeLeftInMillis+"");
                mTimerRunning = true;
            }

            @Override
            public void onFinish() {
                if (!isSent){
                    isSent = true;
                    searchAction();
                    mTimerRunning = false;
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                }
            }
        }.start();
    }


    public void locationTimer(){
        Log.d("Timer" , "City Timer has started.");
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis , 500) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                Log.d("Timer" , "time left: " + mTimeLeftInMillis+"");
                mTimerRunning = true;
            }

            @Override
            public void onFinish() {
                if (!isSent){
                    isSent = true;
                    searchAction();
                    mTimerRunning = false;
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                }
            }
        }.start();
    }
    public void stopTimer(){
        if (mCountDownTimer == null){
            mTimerRunning = false;
        }else {
            Log.d("Timer" , "Timer has stopped.");
            mCountDownTimer.cancel();
            mTimerRunning = false;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            isSent=false;
        }
    }


    public void radioAction(RadioGroup group, int checkedId){
        View radioButton = radio.findViewById(checkedId);
        int index = radio.indexOfChild(radioButton);

        switch (index) {
            case 0:
                cityName.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                latEdit.setVisibility(View.INVISIBLE);
                lonEdit.setVisibility(View.INVISIBLE);
                isByCity = true;
                stopTimer();
                break;
            case 1: // secondbutton

                latEdit.setVisibility(View.VISIBLE);
                lonEdit.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                cityName.setVisibility(View.INVISIBLE);
                isByCity = false;
                stopTimer();
                break;
        }
    }


    public void expandAction(){
        if (currentWeather.getVisibility() == View.VISIBLE) {

            TransitionManager.beginDelayedTransition(currentState,
                    new AutoTransition());
            currentWeather.setVisibility(View.GONE);
            currentExpand.setImageResource(R.drawable.ic_baseline_expand_more_24);
            sharedPref.edit().putBoolean(getString(R.string.is_hide) , true).apply();
        } else {
            TransitionManager.beginDelayedTransition(currentState,
                    new AutoTransition());
            currentWeather.setVisibility(View.VISIBLE);
            currentExpand.setImageResource(R.drawable.ic_baseline_expand_less_24);
            sharedPref.edit().putBoolean(getString(R.string.is_hide) , false).apply();
        }
    }

    public void searchAction(){
        if (isByCity) {
            Functions.toast(getContext(), " City Name");
            String inputCity = cityName.getText().toString();
            try {
                weatherViewModel.refresh(inputCity);
            } catch (Exception e) {
                Functions.toast(getContext(), "Invalid City Name");
            }
        } else {
            String latStr = latEdit.getText().toString();
            String lonStr = lonEdit.getText().toString();
            try {
                weatherViewModel.refresh(Double.parseDouble(latStr), Double.parseDouble(lonStr));
            } catch (Exception e) {
                Functions.toast(getContext(), "Invalid Lat or Lon");
            }
        }

        hideKeyboard();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherViewModel.getOpenWeatherMapLiveData()
                .observe(getViewLifecycleOwner(), openWeatherMap -> {
                    if (openWeatherMap != null) {
                        weatherAdaptor.setDailies(openWeatherMap.getDaily());
                        changeCurrentWeatherStatus(openWeatherMap);
                    }else {
                        weatherAdaptor.setDailies(new ArrayList<>());
                        changeCurrentWeatherStatus(null);
                    }
                });

        weatherViewModel.getMessage()
                .observe(getViewLifecycleOwner(), s -> Functions.toast(getContext(), s));
    }


    public void changeCurrentWeatherStatus(OpenWeatherMap openWeatherMap) {
        if (openWeatherMap!=null) {
            Daily current = openWeatherMap.getCurrent();
            currentTemp.setText(":\t".concat(current.getTemp().getMax() + ""));
            currentPresure.setText(":\t".concat(current.getPressure() + ""));
            currentWindSpeed.setText(":\t".concat(current.getWindSpeed() + ""));
            currentWindDirection.setText(":\t".concat(current.getWindDeg() + ""));
            currentHumidity.setText(":\t".concat(current.getHumidity() + ""));
            currentStateView.setText(current.getWeather().get(0).getDescription());
            String iconCode = current.getWeather().get(0).getIcon();
            Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").
                    placeholder(R.drawable.ic_launcher_background).into(currentImage);

            currentTime.setText(":\t".concat(convertTime(current.getDt())));
            currentLocation.setText(":\t".concat(openWeatherMap.getLat() + "| " + openWeatherMap.getLon()));
        }else {
            currentTemp.setText(getString(R.string.default_value));
            currentPresure.setText(getString(R.string.default_value));
            currentWindDirection.setText(getString(R.string.default_value));
            currentWindSpeed.setText(getString(R.string.default_value));
            currentHumidity.setText(getString(R.string.default_value));
            currentStateView.setText(getString(R.string.default_value));
            currentTime.setText(getString(R.string.default_value));
            currentLocation.setText(getString(R.string.default_value));
            currentImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }


    public String convertTime(long input) {
        long time = input * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(" MMM/dd HH:mm:ss");
        return format.format(date);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
