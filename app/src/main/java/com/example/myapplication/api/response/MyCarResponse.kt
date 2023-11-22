package com.example.myapplication.api.response

import java.io.Serializable


data class MyCarResponse(
    val `data`: List<CarData>,
    val message: String,
    val success: Boolean
)

data class CarData(
    val chasis_number: String,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val insurance_number: String,
    val insurance_type: String,
    val insured_date: String,
    val insurer: String,
    val owner_name: String,
    val owner_phone: Long,
    val plate_number: String,
    val status: Int,
    val updated_at: String,
    val vehicle_code: String,
    val vehicle_name: String,
    val vehicle_photo: String,
    val vehicle_photo_name: String
): Serializable