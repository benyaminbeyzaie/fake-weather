package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Entity(tableName = "coord_response")
@Generated("jsonschema2pojo")
public class CoordResponse {
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
}