package ir.ben.fakeweather.view_models;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.repository.WeatherRepository;
import ir.ben.fakeweather.utils.Functions;

public class WeatherViewModel extends AndroidViewModel {
    private final WeatherRepository repository;
    private final LiveData<OpenWeatherMap> openWeatherMapLiveData;
    private final MutableLiveData<String> message;

    public WeatherViewModel(Application application) {
        super(application);
        repository = new WeatherRepository(application);
        openWeatherMapLiveData = repository.getOpenWeatherMap();
        message = repository.getMessage();
    }

    public void refresh(double lat, double lon) {
        if (Functions.isNetworkAvailable(getApplication())){
            repository.refreshOpenWeatherMapDataWithNetwork(lat, lon);
        }else {
            repository.refreshOpenWeatherMapData(lat, lon);
            message.postValue("You are offline!");
        }
    }

    public void refresh(String cityName) {
        if (Functions.isNetworkAvailable(getApplication())){
            repository.refreshWeatherDataByCityName(cityName);
        }else {
            repository.refreshOpenWeatherMapData(cityName);
            message.postValue("You are offline!");
        }
    }

    public LiveData<OpenWeatherMap> getOpenWeatherMapLiveData() {
        return openWeatherMapLiveData;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }
}
