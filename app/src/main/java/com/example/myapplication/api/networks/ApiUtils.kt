package com.example.myapplication.api.networks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

    class ApiUtils {
        companion object {
            fun checkInternet(context: Context): Boolean {
                val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    val networkInfo = connectivityManager.activeNetworkInfo
                    if (networkInfo != null) {

                        return networkInfo.isConnected
                    }
                } else {
                    val network = connectivityManager.activeNetwork
                    if (network != null) {
                        val networkCapabilities =
                                connectivityManager.getNetworkCapabilities(network)
                        if (networkCapabilities != null) {
                            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(
                                    NetworkCapabilities.TRANSPORT_WIFI
                            )
                        }
                    }
                }
                return false
            }
        }
    }
