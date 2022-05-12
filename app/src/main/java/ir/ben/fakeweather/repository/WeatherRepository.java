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

    public WeatherRepository(Application application) {
        db = AppDatabase.getDatabase(application);
        message = new MutableLiveData<>();
        openWeatherMap = new MutableLiveData<>();
    }

    public void refreshWeatherDataByCityName(String cityName) {
        NetworkService.getInstance().getMyApi().getWeatherByCityName(cityName).enqueue(new Callback<CoordResponse>() {
            @Override
            public void onResponse(Call<CoordResponse> call, Response<CoordResponse> response) {
                if (response.isSuccessful()) {
                    Double lat = response.body().getLat();
                    Double lon = response.body().getLon();
                    CoordResponse coordResponse = new CoordResponse();
                    coordResponse.setLat(lat);
                    coordResponse.setLon(lon);
                    coordResponse.setCityName(cityName);
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        if (db.coordResponseDao().getByCityName(cityName).size() > 0) {
                            db.coordResponseDao().deleteWithCityName(cityName);
                        }
                        db.coordResponseDao().insert(response.body());
                    });
                    refreshOpenWeatherMapDataWithNetwork(coordResponse.getLat(), coordResponse.getLon());
                }
            }

            @Override
            public void onFailure(Call<CoordResponse> call, Throwable t) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    if (db.coordResponseDao().getByCityName(cityName).size() > 0) {
                        CoordResponse cache = db.coordResponseDao().getByCityName(cityName).get(0);
                        refreshOpenWeatherMapDataWithNetwork(cache.getLat(), cache.getLon());
                    }
                });
            }
        });
    }

    public void refreshOpenWeatherMapDataWithNetwork(double lat, double lon) {
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
                        db.openWeatherMapDao().insert(response.body());
                        int id = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon).getId();
                        Log.d("API", "id after insert: " + id);
                        response.body().getCurrent().setOpenWeatherMapFk(id);
                        db.dailyDao().insert(response.body().getCurrent());
                        int currentId = db.dailyDao().getWithFk(id).get(0).getId();
                        response.body().getCurrent().getTemp().setDailyFk(currentId);
                        db.tempDao().insert(response.body().getCurrent().getTemp());
                        for (Weather weather :
                                response.body().getCurrent().getWeather()) {
                            weather.setDailyFk(currentId);
                            db.weatherDao().insert(weather);
                        }

                        for (Daily daily : response.body().getDaily()
                        ) {
                            daily.setOpenWeatherMapFk(id);
                            db.dailyDao().insert(daily);
                            int dailyId = db.dailyDao().getWithFk(id).get(db.dailyDao().getWithFk(id).size() - 1).getId();
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
                    refreshOpenWeatherMapData(lat, lon);
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                // TODO call refreshOpenWeatherMapData and check time for cache,
            }
        });
    }

    private void refreshOpenWeatherMapData(double lat, double lon) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            OpenWeatherMap result = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
            if (result == null) {
                return;
            }
            Daily current = db.dailyDao().getWithFk(result.getId()).get(0);
            current.setWeather(db.weatherDao().get(current.getId()));
            result.setCurrent(current);

            List<Daily> dailies = db.dailyDao().getWithFk(result.getId());
            for (Daily daily :
                    dailies) {
                daily.setWeather(db.weatherDao().get(daily.getId()));
            }

            result.setDaily(dailies);
            openWeatherMap.postValue(result);
            message.postValue("Loaded");
        });
    }

    public LiveData<OpenWeatherMap> getOpenWeatherMap() {
        return openWeatherMap;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
