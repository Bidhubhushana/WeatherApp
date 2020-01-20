package com.weatherapp.presenter

import android.content.Context
import android.util.Log
import com.weatherapp.apimanager.ApiService
import com.weatherapp.apimanager.Retrofit2Manager
import com.weatherapp.model.WeatherDetail
import com.weatherapp.view.WeatherApiView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetWeatherPresenter(private val context: Context,private val location:String,val view:WeatherApiView) : Callback<WeatherDetail> {

    private val apiService: ApiService = Retrofit2Manager.getRetrofit().create(ApiService::class.java)
    private var call: Call<WeatherDetail>? = null


    init {


        call = apiService.getWeatherDetail(location)

        call?.enqueue(this)
    }


    override fun onResponse(call: Call<WeatherDetail>, response: Response<WeatherDetail>) {
        Log.d(TAG,"onResponse")
        Log.d(TAG,"onResponse ${response.raw()}")
        view.onApiSuccessOfWeather(response.body())

    }

    override fun onFailure(call: Call<WeatherDetail>, t: Throwable) {

        Log.e(TAG,"error")
        view.onApiFailureOfWeather(t.localizedMessage)

    }



    companion object {

        private val TAG = GetWeatherPresenter::class.java.simpleName
    }
}



