package com.weatherapp.presenter



import android.content.Context
import android.util.Log
import com.weatherapp.apimanager.ApiService
import com.weatherapp.apimanager.Retrofit2Manager
import com.weatherapp.model.WeatherDetail
import com.weatherapp.view.ForeCastWeatherView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetForeCastWeatherDetails(private val context: Context,private val location:String,val view:ForeCastWeatherView) : Callback<WeatherDetail> {

    private val apiService: ApiService = Retrofit2Manager.getRetrofit().create(ApiService::class.java)
    private var call: Call<WeatherDetail>? = null


    init {


        call = apiService.getWeatherForeCastDetail(location,5)

        call?.enqueue(this)
    }


    override fun onResponse(call: Call<WeatherDetail>, response: Response<WeatherDetail>) {
        Log.d(TAG,"onResponse")
        Log.d(TAG,"onResponse ${response.raw()}")
        view.onApiSuccessOfForeCastWeather(response)

    }

    override fun onFailure(call: Call<WeatherDetail>, t: Throwable) {

        Log.e(TAG,"error")
        view.onApiFailureOfForeCastWeather(t.localizedMessage)

    }



    companion object {

        private val TAG = GetWeatherPresenter::class.java.simpleName
    }
}



