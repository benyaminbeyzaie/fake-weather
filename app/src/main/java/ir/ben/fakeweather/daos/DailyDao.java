package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Daily;

@Dao
public interface DailyDao {
    @Query("SELECT * FROM daily WHERE open_weather_map_fk LIKE :fk")
    List<Daily> getWithFk(int fk);

    @Insert
    void insert(Daily daily);

    @Insert
    void insertAll(List<Daily> dailies);

    @Query("DELETE FROM daily WHERE open_weather_map_fk LIKE :fk")
    void delete(int fk);
}
