package com.example.myapplication.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.api.response.business.Data
import com.example.myapplication.ui.base.BindingActivity
import com.example.myapplication.utils.Constant
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivityBusinessDetailBinding


class BusinessDetailActivity : BindingActivity<ActivityBusinessDetailBinding>() {
    lateinit var data: Data

    override fun getLayout(): Int {
        return R.layout.activity_business_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = intent.getSerializableExtra("data") as Data

        viewBusiness(appSession().getString(Constant.USER_PHONE_NUMBER).toString(),data.id.toString())
        viewBinding.businessNameOnDetail.setText(data.businessname)

        viewBinding.businessDescriptionOnDetail.setText(
            "About: " + data.businessspecializesin.split(
                ","
            ).get(0) ?: "")

        if (data.businessspecializesin.split(",").size > 1) {
            viewBinding.businessSpecializationOnDetails.setText(
                "Speicalises in: " +
                data.businessspecializesin.split(",").get(1) ?: ""
            )
        } else {
            viewBinding.businessSpecializationOnDetails.setText(
                "Speicalises in: " + data.businessspecializesin.split(
                    ","
                ).get(0)
            )
        }

        /*   viewBinding.txtregBusinessPhone.setText(data.businessphone)
           viewBinding.txtregBusinessCellphone.setText(data.businesscellphone)*/



        if (data.postalcode.split(",").size > 1) {
            viewBinding.businessAddressOnDetails.setText(
                "Address: " + data.businessaddress + ", " + data.businesscity + ", " + data.businessregion + ", " + data.postalcode.split(
                    ","
                ).get(0) + ", " + data.postalcode.split(",").get(1)
            )

        } else {
            viewBinding.businessAddressOnDetails.setText(
                "Address: " + data.businessaddress + ", " + data.businesscity + ", " + data.businessregion + ", " + data.postalcode.split(
                    ","
                ).get(0)
            )
        }


        viewBinding.businessOperatinghrsOnDetails.setText("Operating Hours:" + data.operatinghrs)

        Glide.with(this).load(data.logo).placeholder(R.drawable.discoveraddislogo_new)
            .into(viewBinding.ivBusinessImage)


        viewBinding.tvMap.setOnClickListener {
            /*val uri = String.format(Locale.ENGLISH, "geo:%f,%f",
                data.latlng.split(",")[0].toDouble(), data.latlng.split(",")[1].toDouble())
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)*/
            Log.e("dasdasdas", data.latlng.toString())

            val gmmIntentUri = Uri.parse(
                "google.navigation:q= " + data.latlng.split(",")[0] + "," +
                        data.latlng.split(",")[1]
            )
//            val gmmIntentUri = Uri.parse("google.navigation:q= " + 30.7333 +","+
//                    76.7794)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)


//
//            val strUri = "http://maps.google.com/maps?q=loc:${data.latlng.split(",")[0]}," +
//                    "${data.latlng.split(",")[1]} ${data.businessname}"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
//            intent.setClassName(
//                "com.google.android.apps.maps",
//                "com.google.android.maps.MapsActivity")
//            startActivity(intent)

        }
        viewBinding.tvCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + data.businessphone)
            startActivity(intent)
        }

        viewBinding.tvWebsite.setOnClickListener {

            if (data.businessworeda != null) {
                val strs = data?.businessworeda.toString().split(",").toTypedArray()

                if (strs != null)
                if (strs.size > 1){
                    if (strs.get(1).toString().trim().contains("https")) {
                        var path : String = strs.get(1).toString().trim()
                        val uri = Uri.parse(path)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }else  if (strs.get(1).toString().trim().contains("www")) {
                        var path : String = strs.get(1).toString().trim()
                        val uri = Uri.parse("https://" +path)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }  else {
                        Toast.makeText(this, "Website url is invalid!", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this, "Website url is invalid!", Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewBinding.buttonSimilar.setOnClickListener {
            val intent = Intent(this, AllBusinessesActivity::class.java)
            intent.putExtra("category", data.businesscategory)
            intent.putExtra("isSearchHide", true)
            startActivity(intent)
        }

        viewBinding.btnBackTomain.setOnClickListener {
            onBackPressed()
        }

        viewBinding.buttonNearby.setOnClickListener {
            val intent = Intent(this, AllBusinessesActivity::class.java)
            intent.putExtra("distance", true)
            intent.putExtra("isSearchHide", true)
            startActivity(intent)
        }


    }


    fun viewBusiness(id:String,uid:String){
        baseViewModel.repository.businessReview(apiListener(),id,uid).observe(this) {
            if(it.success){

            }
        }
    }
}