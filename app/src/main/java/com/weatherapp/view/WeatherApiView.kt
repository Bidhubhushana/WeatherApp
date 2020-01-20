package com.weatherapp.view

import com.weatherapp.model.WeatherDetail

interface WeatherApiView {

    fun onApiSuccessOfWeather(body: WeatherDetail?)
    fun onApiFailureOfWeather(localizedMessage: String)
}