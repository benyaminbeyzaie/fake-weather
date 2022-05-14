package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Weather;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather WHERE fk LIKE :fk")
    List<Weather> get(long fk);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);

    @Query("DELETE FROM weather WHERE fk LIKE :fk")
    void delete(long fk);
}
