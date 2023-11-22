package com.example.myapplication.api.rest

import android.util.Log
import com.example.myapplication.app.App
import com.example.myapplication.session.Keys
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

import java.util.concurrent.TimeUnit

object RetroClient {
    private var baseUrl= "https://discoveraddisababa.co.za/"
//  private var baseUrl="http://business.mobiledev.in/"
// private var baseUrl="http://staging.discoveraddisababa.co.za/"

    private var mInstance: Retrofit? = null
    private var headerInstance: Retrofit? = null
    private var headerInstanceNew: Retrofit? = null

    private fun getRetroClient(): Retrofit? {
        val interceptor = HttpLoggingInterceptor { s -> Log.e("RetroClient", "requestBody: $s") }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().addHeader("Accept","application/json").build())
        }).addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build()

        mInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return mInstance
    }

    @Synchronized
    fun getInstance(): Retrofit? {
        if (mInstance == null) {
            mInstance = getRetroClient()
        }
        return mInstance
    }

    @Synchronized
    fun getHeaderInstance(): Retrofit? {
        if (headerInstance == null) {
            headerInstance = headerRetroClient()
        }
        return headerInstance
    }



    private fun headerRetroClient(): Retrofit? {

        val interceptor = HttpLoggingInterceptor { s -> Log.e("RetroClient",  "$s") }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                chain.proceed(chain.request().newBuilder().addHeader("Accept","application/json")
                    .addHeader("Authorization","Bearer "+ App.getSharedInstance().getString(
                        Keys.APP_TOKEN)!!).build())
            }).addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).build()



        headerInstance = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(baseUrl)
            .client(client)
            .build()
        return headerInstance
    }

}