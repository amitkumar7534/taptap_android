package com.example.myapplication.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.myapplication.session.Session
import com.example.myapplication.ui.callback.ApiListener

interface BaseCallback {
    fun showFragment(container: Int, fragment: Fragment)
    fun replaceFragment(container: Int, fragment: Fragment)


    fun showToast(message: String)

    @SuppressLint(value = arrayOf("CheckResult"))


    fun openforgotpass()
    fun finishActivity()
    fun openHome()
    fun getCurrentActivity(): Activity
    fun showProgress()
    fun apiListener(): ApiListener
    fun handleError(t: Throwable)
    fun hideProgress()
    fun baseinit()
    fun appSession(): Session
    fun startNewActivity(intent: Intent)
}
