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
    @Query("SELECT * FROM open_weather_map WHERE lat LIKE :lat AND lon LIKE :lon AND saved_at > :ts")
    OpenWeatherMap getOpenWeatherMapWithLatLong(double lat, double lon, long ts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OpenWeatherMap openWeatherMap);

    @Delete
    void delete(OpenWeatherMap openWeatherMap);

    @Query("delete from open_weather_map")
    void deleteAll();
}
