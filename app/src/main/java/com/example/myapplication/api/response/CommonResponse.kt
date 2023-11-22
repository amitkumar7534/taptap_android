package com.example.myapplication.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}