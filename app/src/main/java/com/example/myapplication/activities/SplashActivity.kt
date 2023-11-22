package com.example.myapplication.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.login.LoginActivity
import com.example.myapplication.ui.base.BindingActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.yookoo.discoveraddis.R
import com.yookoo.discoveraddis.databinding.ActivitySplashBinding

class SplashActivity : BindingActivity<ActivitySplashBinding>() {

    var token = ""
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()
        getFcmToken()

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        viewBinding.splashicon.startAnimation(slideAnimation)



        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
                Log.d("MainActivity: ", "Key: $key Value: $value")
            }
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

    }

    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("ATAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.w("ATAG", task.result)
            token = task.result
            appSession().save("token", token.toString())
        })
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(3000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0) {
                        val latestlocIndex = locationResult.locations.size - 1
                        val lati = locationResult.locations[latestlocIndex].latitude
                        val longi = locationResult.locations[latestlocIndex].longitude

                        appSession().save("lat", lati.toFloat())
                        appSession().save("lng", longi.toFloat())


                        if (appSession().getBoolean("login_value") == true) {
                            val intent =
                                Intent(this@SplashActivity, BusinessesDashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

//                        val location = Location("providerNA")
//                        location.longitude = longi
//                        location.latitude = lati

                    } else {
                    }
                }
            }, Looper.getMainLooper())
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            requestLocationPermission()

            //    checkBackgroundLocation()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {

//                        showProgress()

                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            getCurrentLocation()
                        }, 3000)


                        // Now check background location
                        //  checkBackgroundLocation()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }


}