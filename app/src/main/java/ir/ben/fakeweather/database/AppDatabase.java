package ir.ben.fakeweather.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ir.ben.fakeweather.daos.OpenWeatherMapDao;
import ir.ben.fakeweather.models.Current;
import ir.ben.fakeweather.models.Daily;
import ir.ben.fakeweather.models.OpenWeatherMap;
import ir.ben.fakeweather.models.Temp;
import ir.ben.fakeweather.models.Weather;

@Database(entities = {Current.class, Daily.class, OpenWeatherMap.class, Temp.class, Weather.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OpenWeatherMapDao openWeatherMapDao();

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
