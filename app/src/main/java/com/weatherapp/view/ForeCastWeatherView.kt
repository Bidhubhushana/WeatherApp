package com.weatherapp.view

import com.weatherapp.model.WeatherDetail
import retrofit2.Response

interface ForeCastWeatherView {

    fun onApiSuccessOfForeCastWeather(body: Response<WeatherDetail>)
    fun onApiFailureOfForeCastWeather(localizedMessage: String)
}