package com.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherCondition {

    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("code")
    @Expose
    var code: Int? = null

    override fun toString(): String {
        return "WeatherCondition(text=$text, icon=$icon, code=$code)"
    }


}