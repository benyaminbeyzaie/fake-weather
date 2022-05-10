package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Daily;

@Dao
public interface DailyDao {
    @Query("SELECT * FROM daily WHERE open_weather_map_fk LIKE :openWeatherMapFk")
    List<Daily> getAllDailyForOpenWeatherMap(int openWeatherMapFk);

    @Insert
    void insert(Daily daily);

    @Query("delete from daily")
    void deleteAll();
}