package com.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherData {

    @SerializedName("last_updated_epoch")
    @Expose
    var lastUpdatedEpoch: Int? = null
    @SerializedName("last_updated")
    @Expose
    var lastUpdated: String? = null
    @SerializedName("temp_c")
    @Expose
    var tempC: Double? = null
    @SerializedName("temp_f")
    @Expose
    var tempF: Double? = null
    @SerializedName("is_day")
    @Expose
    var isDay: Int? = null
    @SerializedName("condition")
    @Expose
    var condition: WeatherCondition? = null
    @SerializedName("wind_mph")
    @Expose
    var windMph: Double? = null
    @SerializedName("wind_kph")
    @Expose
    var windKph: Double? = null
    @SerializedName("wind_degree")
    @Expose
    var windDegree: Int? = null
    @SerializedName("wind_dir")
    @Expose
    var windDir: String? = null
    @SerializedName("pressure_mb")
    @Expose
    var pressureMb: Double? = null
    @SerializedName("pressure_in")
    @Expose
    var pressureIn: Double? = null
    @SerializedName("precip_mm")
    @Expose
    var precipMm: Double? = null
    @SerializedName("precip_in")
    @Expose
    var precipIn: Double? = null
    @SerializedName("humidity")
    @Expose
    var humidity: Int? = null
    @SerializedName("cloud")
    @Expose
    var cloud: Int? = null
    @SerializedName("feelslike_c")
    @Expose
    var feelslikeC: Double? = null
    @SerializedName("feelslike_f")
    @Expose
    var feelslikeF: Double? = null
    @SerializedName("vis_km")
    @Expose
    var visKm: Double? = null
    @SerializedName("vis_miles")
    @Expose
    var visMiles: Double? = null
    @SerializedName("uv")
    @Expose
    var uv: Double? = null
    @SerializedName("gust_mph")
    @Expose
    var gustMph: Double? = null
    @SerializedName("gust_kph")
    @Expose
    var gustKph: Double? = null

    override fun toString(): String {
        return "WeatherData(lastUpdatedEpoch=$lastUpdatedEpoch, lastUpdated=$lastUpdated, tempC=$tempC, tempF=$tempF, isDay=$isDay, condition=$condition, windMph=$windMph, windKph=$windKph, windDegree=$windDegree, windDir=$windDir, pressureMb=$pressureMb, pressureIn=$pressureIn, precipMm=$precipMm, precipIn=$precipIn, humidity=$humidity, cloud=$cloud, feelslikeC=$feelslikeC, feelslikeF=$feelslikeF, visKm=$visKm, visMiles=$visMiles, uv=$uv, gustMph=$gustMph, gustKph=$gustKph)"
    }


}