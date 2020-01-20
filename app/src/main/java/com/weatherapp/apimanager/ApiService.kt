package com.weatherapp.apimanager

import com.weatherapp.model.WeatherDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


public interface ApiService {

    @GET("current.json?key=90fba02b1ba24b3e90a103059192406")
    abstract fun getWeatherDetail(@Query("q") location: String): Call<WeatherDetail>

    @GET("forecast.json?key=90fba02b1ba24b3e90a103059192406")
    abstract fun getWeatherForeCastDetail(@Query("q") location: String, @Query("days") day: Int): Call<WeatherDetail>

}
