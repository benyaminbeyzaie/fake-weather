
package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "current",
        foreignKeys = {
                @ForeignKey(
                        entity = OpenWeatherMap.class,
                        parentColumns = "id",
                        childColumns = "open_weather_map_fk"
                )})
@Generated("jsonschema2pojo")
public class Current {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "open_weather_map_fk")
    private int openWeatherMapFk;

    @ColumnInfo(name = "dt")
    @SerializedName("dt")
    @Expose
    private Integer dt;

    @ColumnInfo(name = "temp")
    @SerializedName("temp")
    @Expose
    private Double temp;

    @ColumnInfo(name = "feels_like")
    @SerializedName("feels_like")
    @Expose
    private Double feelsLike;

    @ColumnInfo(name = "pressure")
    @SerializedName("pressure")
    @Expose
    private Integer pressure;

    @ColumnInfo(name = "humidity")
    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @ColumnInfo(name = "wind_speed")
    @SerializedName("wind_speed")
    @Expose
    private Double windSpeed;

    @ColumnInfo(name = "wind_deg")
    @SerializedName("wind_deg")
    @Expose
    private Integer windDeg;

    @Ignore
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    public int getId() {
        return id;
    }

    public int getOpenWeatherMapFk() {
        return openWeatherMapFk;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOpenWeatherMapFk(int openWeatherMapFk) {
        this.openWeatherMapFk = openWeatherMapFk;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

}
