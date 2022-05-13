
package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "open_weather_map")
@Generated("jsonschema2pojo")
public class OpenWeatherMap {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    @Expose
    private Double lat;

    @ColumnInfo(name = "lon")
    @SerializedName("lon")
    @Expose
    private Double lon;

    @ColumnInfo(name = "timezone")
    @SerializedName("timezone")
    @Expose
    private String timezone;

    @ColumnInfo(name = "timezone_offset")
    @SerializedName("timezone_offset")
    @Expose
    private Integer timezoneOffset;

    @Ignore
    @SerializedName("current")
    @Expose
    private Daily current;

    @Ignore
    @SerializedName("daily")
    @Expose
    private List<Daily> daily = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Daily getCurrent() {
        return current;
    }

    public void setCurrent(Daily current) {
        this.current = current;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    @Override
    public String toString() {
        return "OpenWeatherMap{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", timezoneOffset=" + timezoneOffset +
                ", current=" + current +
                ", daily=" + daily +
                '}';
    }
}
