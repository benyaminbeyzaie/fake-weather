package ir.ben.fakeweather.utils;



import ir.ben.fakeweather.models.OpenWeatherMap;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("onecall?exclude=hourly,minutely&appid=5d948a7a73fdba8d050fab2ac08472a6&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat , @Query("lon") double lon);

}
