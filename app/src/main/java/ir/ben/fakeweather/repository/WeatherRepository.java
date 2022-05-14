package ir.ben.fakeweather.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Weather;
import ir.ben.fakeweather.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {
    private final AppDatabase db;
    private final MutableLiveData<String> message;
    private final MutableLiveData<OpenWeatherMap> openWeatherMap;

    private static final long CACHE_TIME = (12 * 60 * 60 * 100);

    public WeatherRepository(Application application) {
        db = AppDatabase.getDatabase(application);
        message = new MutableLiveData<>();
        openWeatherMap = new MutableLiveData<>();
    }

    public void refresh(String cityName) {
        NetworkService.getInstance().getMyApi().getWeatherByCityName(cityName).enqueue(new Callback<CoordResponse>() {
            @Override
            public void onResponse(Call<CoordResponse> call, Response<CoordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null || response.body().getCoord() == null) {
                        message.postValue("City not found!");
                        return;
                    }
                    Double lat = response.body().getCoord().getLat();
                    Double lon = response.body().getCoord().getLon();
                    if (lat == null || lon == null) {
                        message.postValue("City not found!");
                        return;
                    }
                    CoordResponse coordResponse = new CoordResponse();
                    coordResponse.setLat(lat);
                    coordResponse.setLon(lon);
                    coordResponse.setCityName(cityName);
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        if (db.coordResponseDao().getByCityName(cityName).size() > 0) {
                            return;
                        }
                        db.coordResponseDao().insert(coordResponse);
                    });

                    refresh(coordResponse.getLat(), coordResponse.getLon());
                } else {
                    message.postValue("City not found!");
                }
            }

            @Override
            public void onFailure(Call<CoordResponse> call, Throwable t) {
                message.postValue("Loading city from cache");
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    if (db.coordResponseDao().getByCityName(cityName).size() > 0) {
                        CoordResponse cache = db.coordResponseDao().getByCityName(cityName).get(0);
                        refresh(cache.getLat(), cache.getLon());
                    } else {
                        message.postValue("City not found!");
                    }
                });
            }
        });
    }

    public void refresh(double lat, double lon) {
        NetworkService.getInstance().getMyApi().getWeather(lat, lon).enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if (response.isSuccessful()) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        message.postValue("Loading");
                        if (response.body() == null || response.body().getCurrent() == null) {
                            message.postValue("Error: response.body() is null or response.body().getCurrent() is null");
                            return;
                        }
                        OpenWeatherMap lastCache = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
                        if (lastCache != null) {
                            for (Daily daily : db.dailyDao().getWithFk(lastCache.getId())
                            ) {
                                db.tempDao().delete(daily.getId());
                                db.weatherDao().delete(daily.getId());
                            }
                            db.dailyDao().delete(lastCache.getId());
                            db.openWeatherMapDao().delete(lastCache);
                        }
                        response.body().saved_at = System.currentTimeMillis();
                        long openWeatherMapId = db.openWeatherMapDao().insert(response.body());
                        Log.d("API", "id after insert: " + openWeatherMapId);
                        response.body().getCurrent().setOpenWeatherMapFk(openWeatherMapId);
                        long currentId = db.dailyDao().insert(response.body().getCurrent());
                        response.body().getCurrent().getTemp().setDailyFk(currentId);
                        db.tempDao().insert(response.body().getCurrent().getTemp());
                        for (Weather weather :
                                response.body().getCurrent().getWeather()) {
                            weather.setDailyFk(currentId);
                            db.weatherDao().insert(weather);
                        }

                        for (Daily daily : response.body().getDaily()
                        ) {
                            daily.setOpenWeatherMapFk(openWeatherMapId);
                            long dailyId = db.dailyDao().insert(daily);
                            daily.getTemp().setDailyFk(dailyId);
                            db.tempDao().insert(daily.getTemp());
                            for (Weather weather :
                                    daily.getWeather()) {
                                weather.setDailyFk(dailyId);
                                weather.setId(0);
                                Log.d("DATA", "weather.id: " + weather.getId());
                                db.weatherDao().insert(weather);
                            }
                        }
                    });
                    Log.d("REPO", "net: " + response.body());
                    message.postValue("Loaded from internet");
                    openWeatherMap.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("REPO", "Error...");
                refreshFromCache(lat, lon);
                message.postValue("Loaded from cache");
            }
        });
    }

    public void refreshFromCache(double lat, double lon) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Log.d("REPO", "Reading from database");

            OpenWeatherMap result = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
            if (result == null) {
                Log.d("REPO", "res is null and return!");

                return;
            }
            Log.d("REPO", "res: " + result);

            Daily current = db.dailyDao().getWithFk(result.getId()).get(0);
            current.setWeather(db.weatherDao().get(current.getId()));
            current.setTemp(db.tempDao().getTemp(current.getId()));
            result.setCurrent(current);

            List<Daily> dailies = db.dailyDao().getWithFk(result.getId()).subList(1, db.dailyDao().getWithFk(result.getId()).size() - 1);
            for (int i = 0; i < dailies.size(); i++) {
                Daily daily = dailies.get(i);
                daily.setWeather(db.weatherDao().get(daily.getId()));
                daily.setTemp(db.tempDao().getTemp(daily.getId()));
            }

            result.setDaily(dailies);
            Log.d("REPO", "database: " + result);

            openWeatherMap.postValue(result);
            message.postValue("Loaded");
        });
    }

    public LiveData<OpenWeatherMap> getOpenWeatherMap() {
        return openWeatherMap;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }
}
