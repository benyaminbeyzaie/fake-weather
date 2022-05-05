package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Current;

@Dao
public interface DailyDao {
    @Query("SELECT * FROM daily WHERE open_weather_map_fk LIKE :openWeatherMapFk")
    List<Current> getAllDailyForOpenWeatherMap(int openWeatherMapFk);
}
