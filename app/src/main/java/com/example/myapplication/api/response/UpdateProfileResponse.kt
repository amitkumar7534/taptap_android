package com.example.myapplication.api.response

data class UpdateProfileResponse(
    val `data`: UserData,
    val message: String,
    val success: Boolean
)

data class UserData(
    val _id: Int,
    val device_id: String,
    val device_token: String,
    val full_name: String,
    val phone_no: Long,
    val profile_pic: String
)