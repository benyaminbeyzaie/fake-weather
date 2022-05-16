
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
    private long id;

    @ColumnInfo(name = "daily_fk")
    private long dailyFk;

    @ColumnInfo(name = "min")
    @SerializedName("min")
    @Expose
    private Double min;

    @ColumnInfo(name = "max")
    @SerializedName("max")
    @Expose
    private Double max;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDailyFk() {
        return dailyFk;
    }

    public void setDailyFk(long dailyFk) {
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

    @Override
    public String toString() {
        return "Temp{" +
                "id=" + id +
                ", dailyFk=" + dailyFk +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
