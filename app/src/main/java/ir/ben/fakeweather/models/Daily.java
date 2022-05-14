
package ir.ben.fakeweather.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "daily",
        foreignKeys = {
                @ForeignKey(
                        entity = OpenWeatherMap.class,
                        parentColumns = "id",
                        childColumns = "open_weather_map_fk"
                )})
@Generated("jsonschema2pojo")
public class Daily {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "open_weather_map_fk")
    private int openWeatherMapFk;

    @ColumnInfo(name = "dt")
    @SerializedName("dt")
    @Expose
    private Integer dt;

    @ColumnInfo(name = "temp")
    @JsonAdapter(TempDes.class)
    @SerializedName("temp")
    @Ignore
    private Temp temp;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenWeatherMapFk() {
        return openWeatherMapFk;
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

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "id=" + id +
                ", openWeatherMapFk=" + openWeatherMapFk +
                ", dt=" + dt +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDeg=" + windDeg +
                ", weather=" + weather +
                '}';
    }
    public static class TempDes implements JsonDeserializer<Temp> {
        @Override
        public Temp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            Log.d("Repo", "min json: " + json.toString());
            if (json.isJsonObject()) {
                Temp temp = new Temp();
                temp.setMax(json.getAsJsonObject().get("max").getAsDouble());
                temp.setMin(json.getAsJsonObject().get("min").getAsDouble());
                return temp;
            } else {
                Temp temp = new Temp();
                temp.setMax(json.getAsDouble());
                temp.setMin(json.getAsDouble());
                return temp;
            }
        }
    }

    public static class MaxDes implements JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Log.d("Repo", "max: " + json.toString());
            if (json.isJsonObject()) {
                return json.getAsJsonObject().get("max").getAsDouble();
            }
            return json.getAsDouble();
        }
    }

    public String getIcon(){
        if (getWeather().size() > 0){
            return getWeather().get(0).getIcon();
        }else {
            return "";
        }
    }

    public String getDescription(){
        if (getWeather().size() > 0){
            return getWeather().get(0).getDescription();
        }else {
            return "";
        }
    }
}


