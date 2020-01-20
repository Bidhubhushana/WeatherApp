package com.weatherapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.weatherapp.R
import com.weatherapp.model.ForeCastDay


class ForeCastDayAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mDataList: MutableList<ForeCastDay>? = ArrayList()


    fun refreshAdapter(list: MutableList<ForeCastDay>) {
        mDataList?.addAll(list)
    }


    override fun getItemCount(): Int {
        return if (mDataList == null)
            0
        else
            mDataList?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.forecast_itemlayout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindItemViewHolder(holder, position)
    }


    private fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var dataHolder = holder as ItemViewHolder
        dataHolder.location?.text = mDataList!![position].date

        val dayOfTheWeek = DateFormat.format("EEEE", mDataList!![position].dateEpoch?.toLong()!! * 1000) as String
        dataHolder.location?.text = dayOfTheWeek

        dataHolder.temperature?.text = mDataList!![position].day?.maxtempC.toString()  +"C"

    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var location: TextView? = null
        var temperature: TextView? = null

        init {
            val roboto_regular = Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")

            location = itemView.findViewById<View>(R.id.tv_location) as TextView
            temperature = itemView.findViewById<View>(R.id.tv_temperature) as TextView

            location?.typeface=roboto_regular
            temperature?.typeface=roboto_regular

        }
    }


    companion object {

        private val TAG = ForeCastDayAdapter::class.java.simpleName

    }
}