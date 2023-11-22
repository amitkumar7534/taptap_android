package com.example.myapplication.login


data class LoginResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
)

data class Data(
    val _id: Int,
    val full_name: String,
    val profile_pic: String,
    val otp: String,
    val phone_no: Long
)








