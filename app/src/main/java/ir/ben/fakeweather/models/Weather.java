
package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "weather")
@Generated("jsonschema2pojo")
public class Weather {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "current_fk")
    private int currentFk;

    @ColumnInfo(name = "daily_fk")
    private int dailyFk;

    @ColumnInfo(name = "main")
    @SerializedName("main")
    @Expose
    private String main;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "icon")
    @SerializedName("icon")
    @Expose
    private String icon;

    public int getCurrentFk() {
        return currentFk;
    }

    public void setCurrentFk(int currentFk) {
        this.currentFk = currentFk;
    }

    public int getDailyFk() {
        return dailyFk;
    }

    public void setDailyFk(int dailyFk) {
        this.dailyFk = dailyFk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
