package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Weather;

@Dao
public interface TempDao {
    @Query("SELECT * FROM `temp` WHERE daily_fk LIKE :dailyFk")
    List<Weather> getAllTempsForDaily(int dailyFk);
}
