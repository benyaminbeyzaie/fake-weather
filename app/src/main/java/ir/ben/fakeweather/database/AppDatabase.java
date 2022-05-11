package ir.ben.fakeweather.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ir.ben.fakeweather.daos.DailyDao;
import ir.ben.fakeweather.daos.OpenWeatherMapDao;
import ir.ben.fakeweather.daos.TempDao;
import ir.ben.fakeweather.daos.WeatherDao;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Temp;
import ir.ben.fakeweather.models.Weather;

@Database(entities = { Daily.class, OpenWeatherMap.class, Weather.class, Temp.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OpenWeatherMapDao openWeatherMapDao();
    public abstract DailyDao dailyDao();
    public abstract WeatherDao weatherDao();
    public abstract TempDao tempDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app-database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
