package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Weather;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather WHERE daily_fk LIKE :dailyFk")
    List<Weather> getAllWeathersForDaily(int dailyFk);

    @Query("SELECT * FROM weather WHERE current_fk LIKE :currentFk")
    List<Weather> getAllWeathersForCurrent(int currentFk);
}
