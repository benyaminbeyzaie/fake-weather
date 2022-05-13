package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Weather;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather WHERE fk LIKE :fk")
    List<Weather> get(int fk);

    @Insert
    void insert(Weather weather);

    @Query("DELETE FROM weather WHERE fk LIKE :fk")
    void delete(int fk);
}
