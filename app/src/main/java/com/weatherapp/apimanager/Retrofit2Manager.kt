package com.weatherapp.apimanager

import com.weatherapp.util.SharedData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit2Manager {

    private val mRetrofit: Retrofit
    private val client: OkHttpClient = OkHttpClient()
    private const val TIME_OUT: Long = 180

    init {


        val clientWith90sTimeout = client.newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
//                .apply { if (BuildConfig.DEBUG) addInterceptor(ChuckInterceptor(VebblerApplication.getAppContext())) }
                .build()


        val gson = GsonBuilder()
                .setLenient()
                .create()

        mRetrofit = Retrofit.Builder()
                .baseUrl(SharedData.weatherUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientWith90sTimeout)
                .build()

        //retrofitAdapter=new Retrofit.Builder().baseUrl(SharedData.REQUEST_STICKER_DATA).addConverterFactory(GsonConverterFactory.create()).client(clientWith90sTimeout).build();

    }

    fun getRetrofit(): Retrofit = mRetrofit

    fun cancelAllRequest() {
        client.dispatcher().cancelAll()
    }
}
