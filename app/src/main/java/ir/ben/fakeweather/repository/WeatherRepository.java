package ir.ben.fakeweather.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ir.ben.fakeweather.database.AppDatabase;
import ir.ben.fakeweather.models.OpenWeatherMap;
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

    public void refreshOpenWeatherMapDataWithNetwork(double lat, double lon) {
        NetworkService.getInstance().getMyApi().getWeather(lat, lon).enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if (response.isSuccessful()) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        OpenWeatherMap lastCache = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
                        if (lastCache != null) {
                            db.openWeatherMapDao().delete(lastCache);
                        }
                        db.openWeatherMapDao().insert(response.body());
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

    private LiveData<OpenWeatherMap> refreshOpenWeatherMapData(double lat, double lon) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            OpenWeatherMap result = db.openWeatherMapDao().getOpenWeatherMapWithLatLong(lat, lon);
            // TODO remember to add objects using foreign key
            openWeatherMap.postValue(result);
        });
        return openWeatherMap;
    }

    public LiveData<OpenWeatherMap> getOpenWeatherMap() {
        return openWeatherMap;
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
