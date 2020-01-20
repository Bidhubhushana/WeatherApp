package com.weatherapp.model



import com.weatherapp.WeatherForeCast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherDetail {

    @SerializedName("location")
    @Expose
    var location: WeatherLocation? = null
    @SerializedName("current")
    @Expose
    var current: WeatherData? = null
    @SerializedName("forecast")
    @Expose
     val forecast: WeatherForeCast? = null

    override fun toString(): String {
        return "WeatherDetail(location=$location, current=$current)"
    }


}