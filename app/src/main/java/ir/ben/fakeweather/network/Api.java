package ir.ben.fakeweather.network;

import ir.ben.fakeweather.models.OpenWeatherMap;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @GET("onecall?exclude=hourly,minutely&appid=5d948a7a73fdba8d050fab2ac08472a6&units=metric")
    Call<OpenWeatherMap> getWeather(@Query("lat") double lat , @Query("lon") double lon);
}
