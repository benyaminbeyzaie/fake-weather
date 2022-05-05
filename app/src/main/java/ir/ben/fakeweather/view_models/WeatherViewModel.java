package ir.ben.fakeweather.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.repository.WeatherRepository;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository repository;
    private final LiveData<OpenWeatherMap> openWeatherMapLiveData;
    private final LiveData<String> message;

    public WeatherViewModel(Application application) {
        super(application);
        repository = new WeatherRepository(application);
        openWeatherMapLiveData = repository.getOpenWeatherMap();
        message = repository.getMessage();
    }

    public void refresh(double lat, double lon) {
        repository.refreshOpenWeatherMapDataWithNetwork(lat, lon);
    }
}
