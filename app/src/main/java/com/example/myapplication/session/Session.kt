package com.example.myapplication.session

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Session(var context: Context) {

    private var mSharedPref: SharedPreferences? = context.getSharedPreferences(
        Keys.PREF_NAME,
            Context.MODE_PRIVATE
    )
    private var editor: SharedPreferences.Editor? = null


    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var mPreference: Session


        fun sharedInstance(c: Context) {
            mPreference = Session(c)
        }

        fun getInstance(): Session = mPreference

    }


    @SuppressLint("CommitPrefEdits")
    fun saveUserToken(token: String?, isLogin: Boolean) {
        editor = mSharedPref?.edit()
        save(Keys.APP_TOKEN, token)
        save(Keys.USER_IS_LOGIN, isLogin)
    }




    @SuppressLint("CommitPrefEdits")
    fun savePosition(position: String) {
        editor = mSharedPref?.edit()
        save("position", position)
    }

    fun saveLatLng(){

    }



    @SuppressLint("CommitPrefEdits")
    fun save(key: String?, `val`: String?) {
        editor = mSharedPref!!.edit()
        editor!!.putString(key, `val`)
        editor!!.apply()
    }
    fun save(key: String?, `val`: Int) {
        editor = mSharedPref!!.edit()
        editor!!.putInt(key, `val`)
        editor!!.apply()
    }

    fun save(key: String?, `val`: Float) {
        editor = mSharedPref!!.edit()
        editor!!.putFloat(key, `val`)
        editor!!.apply()
    }


    @SuppressLint("CommitPrefEdits")
    fun save(key: String?, value: Boolean) {
        editor = mSharedPref!!.edit()
        editor!!.putBoolean(key, value)
        editor!!.apply()
    }

    fun getString(s1: String?): String? {
        return mSharedPref!!.getString(s1, "{}")
    }fun getString1(s1: String?): String? {
        return mSharedPref!!.getString(s1, "[]")
    }
     fun getBoolean(s1: String?): Boolean {
        return mSharedPref!!.getBoolean(s1, false)
    }

    fun getInt(s1: String?): Int {
        return mSharedPref!!.getInt(s1, 0)
    }

    fun getFloat(s1: String?): Float {
        return mSharedPref!!.getFloat(s1, 0.0F)
    }

    fun isLogin(): Boolean {
        return mSharedPref!!.getBoolean(Keys.USER_IS_LOGIN, false)
    }

    fun logout() {
        saveUserToken("",false)
        //editor!!.clear()
        editor!!.commit()
    }




}