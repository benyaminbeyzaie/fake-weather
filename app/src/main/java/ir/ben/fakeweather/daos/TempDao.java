package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Temp;
import ir.ben.fakeweather.models.Weather;

@Dao
public interface TempDao {
    @Query("SELECT * FROM tempDB WHERE daily_fk LIKE :dailyFk")
    Temp getTemp(int dailyFk);

    @Query("DELETE FROM tempDB WHERE daily_fk LIKE :fk")
    void delete(int fk);

    @Insert
    void insert(Temp temp);
}
