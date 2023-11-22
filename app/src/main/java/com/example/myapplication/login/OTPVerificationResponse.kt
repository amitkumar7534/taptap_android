package com.example.myapplication.login

data class OTPVerificationResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
)