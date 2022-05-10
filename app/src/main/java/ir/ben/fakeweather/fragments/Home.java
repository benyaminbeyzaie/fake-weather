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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ir.ben.fakeweather.R;
import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.Current;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Temp;
import ir.ben.fakeweather.models.Weather;
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
    List<Daily> dailyList = new ArrayList<>();

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

        if (isNetworkAvailable(getContext()))
            getWeatherDataByCityName(cityName);
        else {
            Toast.makeText(getContext(),
                    "Check internet connection",
                    Toast.LENGTH_LONG).show();
            updateFromCache();

        }
        return view;
    }

    private void updateFromCache() {
        if (getLon() != null){
            try {
                setWeatherDataByLatLon(getLat(), getLon());
            } catch (InterruptedException e) {

            }
        }
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
                updateFromCache();
            }
        });
    }

    private void updateLastData(OpenWeatherMap weather) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.weatherDao().deleteAll();
                db.tempDao().deleteAll();
                db.dailyDao().deleteAll();
                db.current().deleteAll();
                db.openWeatherMapDao().deleteAll();

                weather.setId(randomIdGen());
                db.openWeatherMapDao().insert(weather);


                for (Daily daily : weather.getDaily()) {
                    daily.setOpenWeatherMapFk(weather.getId());
                    daily.setId(randomIdGen());
                    db.dailyDao().insert(daily);

                    daily.getTemp().setDailyFk(daily.getId());
                    db.tempDao().insert(daily.getTemp());

                    for (Weather weather1 : daily.getWeather()) {
                        weather1.setDailyFk(daily.getId());
                        weather1.setId(randomIdGen());
                        db.weatherDao().insert(weather1);
                    }
                }

                weather.getCurrent().setId(randomIdGen());
                weather.getCurrent().setOpenWeatherMapFk(weather.getId());
                db.current().insert(weather.getCurrent());

                for (Weather weather1 : weather.getCurrent().getWeather()) {
                    weather1.setCurrentFk(weather.getCurrent().getId());
                    weather1.setId(randomIdGen());
                    db.weatherDao().insert(weather1);
                }

            }
        });

    }

    public void setWeatherDataByLatLon(Double lat, Double lon) throws InterruptedException {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OpenWeatherMap openWeather = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
                openWeather.setCurrent(db.current().getAllCurrentsWithFk(openWeather.getId()).get(0));
                List<Daily> dailies = new ArrayList<>();

                for (Daily daily : db.dailyDao().getAllDailyForOpenWeatherMap(openWeather.getId())) {
                    List<Temp> temps = db.tempDao().getAllTempsForDaily(daily.getId());
                    daily.setTemp(temps.get(0));


                    List<Weather> allWeathersForDaily = db.weatherDao().getAllWeathersForDaily(daily.getId());
                    daily.setWeather(allWeathersForDaily);

                    dailies.add(daily);
                }
                openWeather.setDaily(dailies);
                Current current = db.current().getAllCurrentsWithFk(openWeather.getId()).get(0);
                List<Weather> allWeathersForDaily = db.weatherDao().getAllWeathersForCurrent(current.getId());
                current.setWeather(allWeathersForDaily);
                openWeather.setCurrent(current);
                dailyList = dailies;
            }
        });

        Thread.sleep(15000);
        weatherAdaptor.setDailies(dailyList);

    }

    public int randomIdGen(){
        return ThreadLocalRandom.current().nextInt(20, 1000) + 5 + new Random().nextInt(30);
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
