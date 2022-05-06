package ir.ben.fakeweather.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.network.Api;
import ir.ben.fakeweather.network.NetworkService;
import ir.ben.fakeweather.utils.WeatherAdaptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {


    TextView currentTime, currentHumidity, currentMaxtemp, currentMintemp, currentWind, currentPressure;
    RecyclerView recyclerView;
    WeatherAdaptor weatherAdaptor;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        currentTime = view.findViewById(R.id.time_id_current);
        currentHumidity = view.findViewById(R.id.humidity_id_current);
        currentMaxtemp = view.findViewById(R.id.maxtemp_id_current);
        currentMintemp = view.findViewById(R.id.mintemp_id_current);
        currentWind = view.findViewById(R.id.wind_id_current);
        currentPressure = view.findViewById(R.id.pressure_id_current);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager( getContext()));
        weatherAdaptor = new WeatherAdaptor();
        recyclerView.setAdapter(weatherAdaptor);
        getWeatherData(51.5283,-0.3818);
        return view;
    }



    public void getWeatherData(double lat , double lon){
        NetworkService.getInstance().getMyApi().getWeather(lat, lon).enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if (response.isSuccessful()) {
                    List<Daily> daily = response.body().getDaily();
                    weatherAdaptor.setDailies(daily);
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                // TODO call refreshOpenWeatherMapData and check time for cache,
            }
        });
    }



}