
package ir.ben.fakeweather.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "tempDB",
        foreignKeys = {
                @ForeignKey(
                        entity = Daily.class,
                        parentColumns = "id",
                        childColumns = "daily_fk"
                )})
@Generated("jsonschema2pojo")
public class Temp {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "daily_fk")
    private int dailyFk;

    @ColumnInfo(name = "min")
    @SerializedName("min")
    @Expose
    private Double min;

    @ColumnInfo(name = "max")
    @SerializedName("max")
    @Expose
    private Double max;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDailyFk() {
        return dailyFk;
    }

    public void setDailyFk(int dailyFk) {
        this.dailyFk = dailyFk;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

}
