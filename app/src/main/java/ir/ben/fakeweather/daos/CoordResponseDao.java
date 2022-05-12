package ir.ben.fakeweather.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.Current;

@Dao
public interface CoordResponseDao {
    @Query("SELECT * FROM coord_response WHERE cityName LIKE :cityName")
    List<CoordResponse> getByCityName(String cityName);

    @Insert
    void insert(CoordResponse coordResponse);

    @Query("delete from coord_response where cityName like :cityName")
    void deleteWithCityName(String cityName);

}
