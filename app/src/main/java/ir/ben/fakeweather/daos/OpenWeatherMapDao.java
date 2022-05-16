package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Weather;

@Dao
public interface OpenWeatherMapDao {
    @Query("SELECT * FROM open_weather_map WHERE lat LIKE :lat AND lon LIKE :lon")
    OpenWeatherMap getOpenWeatherMapWithLatLong(double lat, double lon);

    @Query("delete FROM open_weather_map WHERE lat LIKE :lat AND lon LIKE :lon")
    void delete(double lat, double lon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(OpenWeatherMap openWeatherMap);

    @Query("delete from open_weather_map")
    void deleteAll();
}
