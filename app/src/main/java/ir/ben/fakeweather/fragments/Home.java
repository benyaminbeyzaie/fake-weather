package ir.ben.fakeweather.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

    TextView currentTime , currentLocation , currentTemp , currentHumidity , currentPresure , currentWindSpeed,currentWindDirection;
    ImageView currentImage;
    TextView currentStateView;


    private final String LAT = "lat";
    private final String LON = "lon";
    private final String CITY = "city";


    private WeatherViewModel weatherViewModel;
    LiveData<OpenWeatherMap> openWeatherMapLiveData ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout darkThemeLayout = new LinearLayout(new ContextThemeWrapper(getContext(), R.style.Theme_FakeWeather3));

        sharedPref = getContext().getSharedPreferences("Weather", MODE_PRIVATE);
        editor = sharedPref.edit();
        // Inflate the layout for this fragment
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
        currentImage=view.findViewById(R.id.imageView_current);
        currentStateView = view.findViewById(R.id.status_id_current);



        radio = (RadioGroup) view.findViewById(R.id.radio_button);

        cityName = view.findViewById(R.id.city_name);
        latEdit = view.findViewById(R.id.lat);
        lonEdit = view.findViewById(R.id.lon);

        currentState = view.findViewById(R.id.current_card_view);
        currentWeather = view.findViewById(R.id.cardView_current);
        currentState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentWeather.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(currentState,
                            new AutoTransition());
                    currentWeather.setVisibility(View.GONE);
//                    arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(currentState,
                            new AutoTransition());
                    currentWeather.setVisibility(View.VISIBLE);
//                    arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
            }
        });






        searchButton = view.findViewById(R.id.search_button);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        cityName.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.VISIBLE);
                        latEdit.setVisibility(View.INVISIBLE);
                        lonEdit.setVisibility(View.INVISIBLE);
                        isByCity = true;
                        break;
                    case 1: // secondbutton

                        latEdit.setVisibility(View.VISIBLE);
                        lonEdit.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.VISIBLE);
                        cityName.setVisibility(View.INVISIBLE);
                        isByCity = false;
                        break;
                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        weatherAdaptor = new WeatherAdaptor();
        recyclerView.setAdapter(weatherAdaptor);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isByCity) {
                    Functions.toast(getContext() , " City Name");

                    String inputCity = cityName.getText().toString();

                    try {
                        weatherViewModel.refresh(inputCity);
                    }catch (Exception e){
                        Functions.toast(getContext() , "Invalid City Name");
                    }
                } else {
                    String latStr = latEdit.getText().toString();
                    String lonStr = lonEdit.getText().toString();

                    try {
                        weatherViewModel.refresh(Double.parseDouble(latStr) , Double.parseDouble(lonStr));
                    }catch (Exception e){
                        Functions.toast(getContext() , "Invalid Lat or Lon");
                    }

                }
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherViewModel.getOpenWeatherMapLiveData()
                .observe(getViewLifecycleOwner() , openWeatherMap -> {
                    weatherAdaptor.setDailies(openWeatherMap.getDaily().subList(1, openWeatherMap.getDaily().size()));
//                    weatherAdaptor.setDailies(openWeatherMap.getDaily());
                    changeCurrentWeatherStatus(openWeatherMap);
                });

        weatherViewModel.getMessage()
                .observe(getViewLifecycleOwner() , s -> {
                    Functions.toast(getContext() , s);
                });
    }


    public void changeCurrentWeatherStatus(OpenWeatherMap openWeatherMap){
        Daily current = openWeatherMap.getCurrent();
        currentTemp.setText(":\t" + current.getTemp().getMax() + "");
        currentPresure.setText(":\t" + current.getPressure() + "");
        currentWindSpeed.setText(":\t" + current.getWindSpeed() + "");
        currentWindDirection.setText(":\t" + current.getWindDeg() + "");
        currentHumidity.setText(":\t" + current.getHumidity() + "");
        currentStateView.setText(current.getWeather().get(0).getDescription());
        String iconCode = current.getWeather().get(0).getIcon();
        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").
                placeholder(R.drawable.ic_launcher_background).into(currentImage);
    }
}
