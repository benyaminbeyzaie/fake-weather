package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Current;

@Dao
public interface CurrentDao {
    @Query("SELECT * FROM current WHERE open_weather_map_fk LIKE :openWeatherMapFk")
    List<Current> getAllCurrentsWithFk(int openWeatherMapFk);
}
