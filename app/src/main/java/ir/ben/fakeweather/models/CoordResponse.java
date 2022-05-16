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
    private long id;

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

    @ColumnInfo(name = "saved_at")
    public long savedAt = 0;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CoordModel getCoord() {
        return coord;
    }

    public void setCoord(CoordModel coord) {
        this.coord = coord;
    }

    public long getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(long savedAt) {
        this.savedAt = savedAt;
    }

    @Override
    public String toString() {
        return "CoordResponse{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", coord=" + coord +
                '}';
    }
}