package ir.ben.fakeweather.repository;

import android.app.Application;

import ir.ben.fakeweather.database.AppDatabase;

public class WeatherRepository {
    public WeatherRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
    }
}
