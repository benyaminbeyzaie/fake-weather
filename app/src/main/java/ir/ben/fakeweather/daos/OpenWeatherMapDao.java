package ir.ben.fakeweather.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Weather;

public interface OpenWeatherMapDao {
    @Query("SELECT * FROM open_weather_map WHERE lat LIKE :lat AND lon LIKE :lon")
    OpenWeatherMap getOpenWeatherMapWithLatLong(double lat, double lon);

    @Insert
    void insert(OpenWeatherMap openWeatherMap);

    @Delete
    void delete(OpenWeatherMap openWeatherMap);
}
