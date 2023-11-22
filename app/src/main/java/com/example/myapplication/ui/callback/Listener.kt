package com.example.myapplication.ui.callback

import android.view.View


interface ApiListener {
    fun progress(isVisible: Boolean)
    fun msg(msg: String)
}
interface ViewClickCallback {
    fun onClick(position:Int,id:String,type:String)
}

interface DataItemListener<T, R> {
    fun onItem(t:T, r:R);
}


interface OverViewItemListener<T, R> {
    fun onItem(v:View,t:T, r:R);
}

