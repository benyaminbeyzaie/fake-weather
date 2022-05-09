package ir.ben.fakeweather.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.network.NetworkService;
import ir.ben.fakeweather.utils.WeatherAdaptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {


    TextView currentTime, currentHumidity, currentMaxtemp, currentMintemp, currentWind, currentPressure;
    RecyclerView recyclerView;
    WeatherAdaptor weatherAdaptor;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private final String LAT = "lat";
    private final String LON = "lon";
    private final String CITY = "city";

    AppDatabase db;


    public Home(AppDatabase db) {
        this.db = db;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = getContext().getSharedPreferences("Weather", MODE_PRIVATE);
        editor = sharedPref.edit();
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


        String cityName = "cairo";

        saveCityName(cityName);

        updateLastData(null);
//        if (isNetworkAvailable(getContext()))
//            getWeatherDataByCityName(cityName);
//        else
//            Toast.makeText(getContext(),
//                    "Check internet connection",
//                    Toast.LENGTH_LONG).show();
        return view;
    }

    public void saveCityName(String city) {
        editor.putString(CITY, city);
        editor.apply();
    }

    public void saveLatLong(Double lat, Double lon) {
        editor.putString(LAT, lat+"");
        editor.putString(LON, lon+"");
        editor.apply();
    }

    public String getCity() {
        return sharedPref.getString(CITY, null);
    }

    public Double getLat() {
        return convertStrToDouble(sharedPref.getString(LAT, null));
    }

    public Double getLon() {
        return convertStrToDouble(sharedPref.getString(LON, null));
    }

    private Double convertStrToDouble(String str) {
        if (str == null)
            return null;

        return Double.parseDouble(str);
    }

    public void getWeatherDataByLocation(double lat , double lon){
        NetworkService.getInstance().getMyApi().getWeather(lat, lon).enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if (response.isSuccessful()) {
                    List<Daily> daily = response.body().getDaily();
                    updateLastData(response.body());
                    weatherAdaptor.setDailies(daily);
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                // TODO call refreshOpenWeatherMapData and check time for cache,
            }
        });
    }

    private void updateLastData(OpenWeatherMap weather) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.openWeatherMapDao().deleteAll();
                db.openWeatherMapDao().insert(weather);


                OpenWeatherMap openWeather = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(getLat(), getLon());
                System.out.println("hello");
            }
        });

    }


    public void getWeatherDataByCityName(String cityName){
        NetworkService.getInstance().getMyApi().getWeatherByCityName(cityName).enqueue(new Callback<CoordResponse>() {
            @Override
            public void onResponse(Call<CoordResponse> call, Response<CoordResponse> response) {
                if (response.isSuccessful()) {
                    Double lat = response.body().getCoord().getLat();
                    Double lon = response.body().getCoord().getLon();

                    saveLatLong(lat, lon);
                    getWeatherDataByLocation(lat, lon);
                }
            }

            @Override
            public void onFailure(Call<CoordResponse> call, Throwable t) {
                if (getLat() != null)
                    getWeatherDataByLocation(getLat(), getLon());
            }
        });
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
}
