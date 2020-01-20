package com.weatherapp

import com.weatherapp.model.ForeCastDay
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherForeCast {

    @SerializedName("forecastday")
    @Expose
    var forecastday: List<ForeCastDay>? = null

}
