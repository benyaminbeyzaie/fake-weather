package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "coord_response")
@Generated("jsonschema2pojo")
public class CoordResponse {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cityName")
    private String cityName;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    @Expose
    private Double lat;

    @ColumnInfo(name = "lon")
    @SerializedName("lon")
    @Expose
    private Double lon;

    @Ignore
    private CoordModel coord;

    public long saved_at = 0;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CoordModel getCoord() {
        return coord;
    }

    public void setCoord(CoordModel coord) {
        this.coord = coord;
    }

}