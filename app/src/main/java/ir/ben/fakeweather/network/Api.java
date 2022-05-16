package ir.ben.fakeweather.network;

import ir.ben.fakeweather.models.CoordResponse;
import ir.ben.fakeweather.models.OpenWeatherMap;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @GET("onecall?exclude=hourly,minutely&appid=b51f5eb49cad835a5919fd47e072e2b2&units=metric")
    Call<OpenWeatherMap> getWeather(@Query("lat") double lat , @Query("lon") double lon);

    @GET("weather?APPID=b51f5eb49cad835a5919fd47e072e2b2")
    Call<CoordResponse> getWeatherByCityName(@Query("q") String cityName);
}
