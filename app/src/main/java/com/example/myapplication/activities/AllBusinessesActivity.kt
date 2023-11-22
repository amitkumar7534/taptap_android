package com.example.myapplication.activities

import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.example.myapplication.adapter.BusinessesAdapter
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.api.response.business.DistanceResponse
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.ui.callback.ViewClickCallback
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityAllBusinessesBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class AllBusinessesActivity :BindingActivity<ActivityAllBusinessesBinding> () {
    var adapter: BusinessesAdapter? = null
    lateinit var  origin:LatLng
    val distaceList= ArrayList<DistanceResponse>()
    override fun getLayout(): Int {
        return R.layout.activity_all_businesses
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        origin=LatLng(appSession().getFloat("lat").toDouble(), appSession().getFloat("lng").toDouble())
        val dest=LatLng(28.7041, 77.1025)
        var url = getDirectionsUrl(origin, dest)
        val downloadTask = DownloadTask()



        // Start downloading json data from Google Directions API
       // downloadTask.execute(url)

        getAllBusinesses(intent.getStringExtra("category")?:"")
        viewBinding!!.ivBack.setOnClickListener {
            onBackPressed()
        }

 /*       if(intent.getBooleanExtra("isSearchHide",false)){
            viewBinding.clDepartmentsListLnr.visibility=View.GONE
            viewBinding.spinnerCategory.visibility=View.GONE
        }else{
            viewBinding.clDepartmentsListLnr.visibility=View.VISIBLE
        }


        viewBinding.layoutUserView.visibility=View.GONE
        viewBinding.homeCategory.visibility=View.GONE
        viewBinding.title.setText(intent.getStringExtra("category")?:"")

        viewBinding.txtClDepsPhonCardnum.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(v.text.toString()==""){
                    getAllBusinesses("")
                }else {
                    searchBusinesses(v.text.toString())
                }
                return@OnEditorActionListener true
            }
            false
        })*/


    }



    fun getAllBusinesses(category:String){
        baseViewModel.repository.getAllBusiness(apiListener(),category).observe(this) {
            if(it.success){
                if(it.data.isNullOrEmpty()){
                    showToast("No data found")
                }else {
                    distaceList.clear()
                    for (i in it.data.indices){
                        distance(origin.latitude,origin.longitude,it.data[i].latlng.split(",").get(0).toDouble(),it.data[i].latlng.split(",").get(1).toDouble())
                    }

                    Log.e("cdsc ds",distaceList.toString())
                    adapter = BusinessesAdapter(this,intent.getBooleanExtra("distance",false),distaceList,object:ViewClickCallback{
                        override fun onClick(position: Int, id: String,type:String) {
                           showLogoutDialog(id)
                        }

                    })
                    adapter?.list!!.clear()
                    adapter?.list = it.data as ArrayList<Data>
                    viewBinding!!.businessRecyclerView.adapter = adapter
                }
            }
        }
    }
    fun searchBusinesses(keyword:String){
        baseViewModel.repository.seaerchBusiness(apiListener(),keyword).observe(this) {
            if(it.success){

                if(it.data.isNullOrEmpty()){
                    showToast("No data found")
                }else {
                    distaceList.clear()
                    for (i in it.data.indices){
                        distance(origin.latitude,origin.longitude,it.data[i].latlng.split(",").get(0).toDouble(),it.data[i].latlng.split(",").get(1).toDouble())
                    }
                    adapter = BusinessesAdapter(this,false,distaceList,object:ViewClickCallback{
                        override fun onClick(position: Int, id: String,type:String) {
                            showLogoutDialog(id)

                        }

                    })
                    adapter?.list!!.clear()
                    adapter?.list = it.data as ArrayList<Data>
                    viewBinding!!.businessRecyclerView.adapter = adapter
                }
            }
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double) {

        val distance: Float
        val locationA = Location("A")
        locationA.latitude = lat1
        locationA.longitude = lon1

        val locationB = Location("B")
        locationB.latitude = lat2
        locationB.longitude = lon2

        distance = locationA.distanceTo(locationB) / 1000

        val speedIs1KmMinute = 60
        val estimatedDriveTimeInMinutes =( distance / speedIs1KmMinute)*3600
        val walkIs1KmMinute = 5
        val estimatedWalkTimeInMinutes =( distance / walkIs1KmMinute)*3600
        val time =getDurationString(estimatedDriveTimeInMinutes.toInt())
        val time2 =getDurationString(estimatedWalkTimeInMinutes.toInt())
        Log.e("dfdfvd","$distance ${time} $time2" )

        distaceList.add(DistanceResponse(String.format("%.2f", distance).toString(),time.toString(),time2.toString()))


    }

    private fun getDurationString(seconds: Int): String? {
        var seconds = seconds
        val hours = seconds / 3600
        val minutes = seconds % 3600 / 60
        seconds = seconds % 60
        if (hours==0){
          return  twoDigitString(minutes) + " min"

        }else{
            return twoDigitString(hours) + " Hours " + twoDigitString(minutes) + " min"

        }
    }

    private fun twoDigitString(number: Int): String {
        if (number == 0) {
            return "00"
        }
        return if (number / 10 == 0) {
            "0$number"
        } else number.toString()
    }





    fun showLogoutDialog(id:String){
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.are_you_logout))
        builder.setPositiveButton(getString(R.string.yes)
        ) { dialog, which ->
            deleteBusinesses(id)
        }
        builder.setNegativeButton(getString(R.string.no), null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.black))
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.black))
        }
        dialog.show()
    }

    fun deleteBusinesses(keyword:String){
        baseViewModel.repository.deleteBusiness(apiListener(),keyword).observe(this) {
            if(it.success){
                    getAllBusinesses("")
                }
            }
        }



    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String? {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest =
            "destination=" + dest.latitude + "," + dest.longitude + "&mode=driving"

        // Key
        val key = "key=" + "AIzaSyB4vxRdcuw6s3tMkgTOEFU2PAIttYs8ZMM"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        Log.e("dcdscw", "https://maps.googleapis.com/maps/api/directions/$output?$parameters")
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }




    inner class DownloadTask : AsyncTask<String?, Void?, String?>() {

        protected override fun doInBackground(vararg url: String?): String {

            var data = ""
            try {
                data = downloadUrl(url[0]!!)!!
                Log.d("DownloadTask", "DownloadTask : $data")

                val jsonObject = JsonParser().parse(data).getAsJsonObject()
                val data = jsonObject.get("routes").asJsonArray
                val legs = data[0].asJsonObject.get("legs").asJsonArray
                for (i in 0 until legs.size()) {
                    var duration: String =
                        legs[i].asJsonObject.get("duration").asJsonObject.get("text").toString()
                    var distance: String =
                        legs[i].asJsonObject.get("distance").asJsonObject.get("text").toString()
                    Log.e("wefewfw final", "$duration $distance" )

                }


            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }



    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.getInputStream()
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
            Log.d("Exception on download", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

}