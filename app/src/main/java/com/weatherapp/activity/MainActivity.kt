package com.weatherapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.View
import com.weatherapp.R
import com.weatherapp.adapter.ForeCastDayAdapter
import com.weatherapp.location.FusedLocationHandler
import com.weatherapp.model.WeatherDetail
import com.weatherapp.presenter.GetForeCastWeatherDetails
import com.weatherapp.presenter.GetWeatherPresenter
import com.weatherapp.view.ForeCastWeatherView
import com.weatherapp.view.WeatherApiView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), WeatherApiView, ForeCastWeatherView {


    private var locationHandler: FusedLocationHandler? = null
    private var isNetWorkConnected = false

    private var foreCastAdapter: ForeCastDayAdapter? = null
    private var isRequestingLastLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingProgressBar.visibility = View.VISIBLE

        askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),121)

        foreCastAdapter = ForeCastDayAdapter(this)
       /* foreCastRecyclerView.addItemDecoration(DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )*/
        foreCastRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }?.apply { adapter = foreCastAdapter }
            .apply { (foreCastRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false }

        val roboto = Typeface.createFromAsset(this.assets, "fonts/roboto.thin.ttf")
        val roboto_black = Typeface.createFromAsset(this.assets, "fonts/roboto_black.ttf")
        tv_failure.typeface = roboto
        tv_temperature.typeface = roboto
        tv_temperature.typeface = roboto_black
        tv_location.typeface = roboto



        retry_btn.setOnClickListener {

            askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),121)

        }


    }


    private fun formLocationRequest() {

        locationHandler = FusedLocationHandler.getInstance(this@MainActivity)
        locationHandler?.formFusedLocationRequest(
            (1 * 1000).toLong(),
            (1 * 1000).toLong(),
            FusedLocationHandler.ONLY_GPS
        )

        locationHandler?.setLocationSettingsSuccessfulCallback {
            locationHandler?.startLocationUpdates()

        }
        locationHandler?.setOnLocationChangedListener { latitude, longitude ->

            Log.d(TAG, "latitude=$latitude")
            Log.d(TAG, "longitude=$longitude")

            locationHandler?.stopLocationUpdates()

            if (latitude > 0 && longitude > 0) {

                var location = getAddress(latitude, longitude)

                GetWeatherPresenter(this, location!!, this)

                GetForeCastWeatherDetails(this, location, this)

            } else if(isRequestingLastLocation){
                loadingProgressBar.visibility = View.GONE
                tv_failure.visibility = View.VISIBLE
                parent_layout.setBackgroundColor(Color.parseColor("#E85959"))
                layout.setBackgroundColor(Color.parseColor("#E85959"))
                retry_btn.visibility=View.VISIBLE
            }
        }

        isNetWorkConnected = isNetworkConnected()


        locationHandler?.checkLocationSettings(this@MainActivity, !isNetWorkConnected)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    fun getAddress(lat: Double, lng: Double): String? {

        var location: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses[0]
            var add = obj.getAddressLine(0)
            add = add + "\n" + obj.countryName
            add = add + "\n" + obj.countryCode
            add = add + "\n" + obj.adminArea
            add = add + "\n" + obj.postalCode
            add = add + "\n" + obj.subAdminArea
            location = obj.locality
            add = add + "\n" + obj.locality
            add = add + "\n" + obj.subThoroughfare

            Log.v("IGA", "Address$add")
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
        }
        return location
    }


    @SuppressLint("SetTextI18n")
    override fun onApiSuccessOfWeather(body: WeatherDetail?) {
        tv_failure.visibility = View.GONE
        retry_btn.visibility=View.GONE
        parent_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        layout.setBackgroundColor(Color.parseColor("#EEEEEE"))
        location_temp_layout.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE
        tv_temperature.text = body?.current?.tempC.toString() + 0x00B0.toChar()
        tv_location.text = body?.location?.name
    }

    override fun onApiFailureOfWeather(localizedMessage: String) {
        loadingProgressBar.visibility = View.GONE
        tv_failure.visibility = View.VISIBLE
        parent_layout.setBackgroundColor(Color.parseColor("#E85959"))
        layout.setBackgroundColor(Color.parseColor("#E85959"))
        retry_btn.visibility=View.VISIBLE

    }

    override fun onApiSuccessOfForeCastWeather(body: Response<WeatherDetail>) {
        tv_failure.visibility = View.GONE
        retry_btn.visibility=View.GONE
        parent_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        layout.setBackgroundColor(Color.parseColor("#EEEEEE"))
        location_temp_layout.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE

        val list = body.body()?.forecast?.forecastday

        foreCastAdapter?.refreshAdapter(list!!.subList(1, list.size).toMutableList())
        foreCastAdapter?.notifyDataSetChanged()
    }

    override fun onApiFailureOfForeCastWeather(localizedMessage: String) {
        loadingProgressBar.visibility = View.GONE
        tv_failure.visibility = View.VISIBLE
        parent_layout.setBackgroundColor(Color.parseColor("#E85959"))
        layout.setBackgroundColor(Color.parseColor("#E85959"))
        retry_btn.visibility=View.VISIBLE

    }

    private fun isNetworkConnected(): Boolean {

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }

    private fun askForPermissions(permissions: Array<String>, requestCode: Int) {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) !== PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if(permissionsToRequest.isNotEmpty())
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        else
            formLocationRequest()

    }


    override
    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            formLocationRequest()
            loadingProgressBar.visibility = View.VISIBLE
            tv_failure.visibility = View.GONE
            retry_btn.visibility=View.GONE
            parent_layout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            layout.setBackgroundColor(Color.parseColor("#EEEEEE"))
        }
        else if(requestCode == 121){
            loadingProgressBar.visibility = View.GONE
            tv_failure.visibility = View.VISIBLE
            retry_btn.visibility=View.VISIBLE
            parent_layout.setBackgroundColor(Color.parseColor("#E85959"))
            layout.setBackgroundColor(Color.parseColor("#E85959"))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2) {
            if(resultCode == Activity.RESULT_CANCELED) {
                isRequestingLastLocation = true
                locationHandler?.sendLastKnownLocation()
            }
            else {
                isRequestingLastLocation = false
                locationHandler?.startLocationUpdates()
            }
        }
    }
}
