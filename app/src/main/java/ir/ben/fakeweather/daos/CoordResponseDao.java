package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.CoordResponse;

@Dao
public interface CoordResponseDao {
    @Query("SELECT * FROM coord_response WHERE cityName LIKE :cityName AND saved_at > :ts")
    List<CoordResponse> getByCityName(String cityName , long ts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CoordResponse coordResponse);

    @Query("delete from coord_response where cityName like :cityName")
    void deleteWithCityName(String cityName);

}
