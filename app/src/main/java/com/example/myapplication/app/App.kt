package com.example.myapplication.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.api.rest.RestApi
import com.example.myapplication.api.rest.RetroClient
import com.example.myapplication.session.Session
import retrofit2.Retrofit


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Session.sharedInstance(this)
        checkNightMode()

    }


    companion object {
        var mInstance: App = getInstance()

        fun getSharedInstance(): Session = Session.getInstance()

        @Synchronized
        fun getInstance(): App {
            return mInstance
        }

        fun getClient(): Retrofit? {
            return RetroClient.getInstance()
        }



        private fun headerClient(): Retrofit {
            return RetroClient.getHeaderInstance()!!
        }

        fun checkNightMode() {

            val type: String = getSharedInstance().getString("theme").toString()
            when (type) {
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }

        }





        fun apiInterface(): RestApi? {
            return getClient()!!.create(RestApi::class.java)
        }

        fun headerInterface(): RestApi? {
            return headerClient().create(RestApi::class.java)
        }




    }

}