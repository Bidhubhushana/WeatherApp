package com.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForeCastDay {

    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("date_epoch")
    @Expose
    var dateEpoch: Int? = null
    @SerializedName("day")
    @Expose
    var day: ForeCastDayDetails? = null
    @SerializedName("astro")
    @Expose
    var astro: WeatherAstro? = null

}
