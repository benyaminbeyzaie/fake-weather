package ir.ben.fakeweather.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Temp;
import ir.ben.fakeweather.models.Weather;
import ir.ben.fakeweather.network.NetworkService;
import ir.ben.fakeweather.utils.Functions;
import ir.ben.fakeweather.utils.WeatherAdaptor;
import ir.ben.fakeweather.view_models.WeatherViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {


    EditText cityName, latEdit, lonEdit;
    Button searchButton;
    RadioGroup radio;
    RecyclerView recyclerView;
    WeatherAdaptor weatherAdaptor;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    List<Daily> dailyList = new ArrayList<>();
    boolean isByCity = true;

    private final String LAT = "lat";
    private final String LON = "lon";
    private final String CITY = "city";


    private WeatherViewModel weatherViewModel;
    LiveData<OpenWeatherMap> openWeatherMapLiveData ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedPref = getContext().getSharedPreferences("Weather", MODE_PRIVATE);
        editor = sharedPref.edit();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        openWeatherMapLiveData = weatherViewModel.getOpenWeatherMapLiveData();




        radio = (RadioGroup) view.findViewById(R.id.radio_button);

        cityName = view.findViewById(R.id.city_name);
        latEdit = view.findViewById(R.id.lat);
        lonEdit = view.findViewById(R.id.lon);

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

//        openWeatherMapLiveData.observe(getActivity(), new Observer<OpenWeatherMap>() {
//            @Override
//            public void onChanged(OpenWeatherMap openWeatherMap) {
//                weatherAdaptor.setDailies(openWeatherMap.getDaily());
//            }
//        });
        weatherViewModel.refresh(51.5072, 0.1276);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isByCity) {
                    String inputCity = cityName.getText().toString();

                    try {
//                        LiveData<OpenWeatherMap> openWeatherMapLiveData = weatherViewModel.getOpenWeatherMapLiveData();
                        // weatherAdaptor.clear();
                        weatherViewModel.refresh(inputCity);
                        List<Daily> daily = openWeatherMapLiveData.getValue().getDaily();
                    }catch (Exception e){
                        Functions.toast(getContext() , "Invalid City Name");
                    }
                } else {
                    String latStr = latEdit.getText().toString();
                    String lonStr = lonEdit.getText().toString();

                    try {
                        //weatherAdaptor.clear();
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
                });

        weatherViewModel.getMessage()
                .observe(getViewLifecycleOwner() , s -> {
                    Functions.toast(getContext() , s);
                });
    }
}
