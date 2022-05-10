package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.Temp;

@Dao
public interface TempDao {
    @Query("SELECT * FROM tempDB WHERE daily_fk LIKE :dailyFk")
    List<Temp> getAllTempsForDaily(int dailyFk);

    @Insert
    void insert(Temp temp);


    @Query("delete from tempDB")
    void deleteAll();
}
