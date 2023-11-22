package com.example.myapplication.api.response.business

import java.io.Serializable

data class BusinessResponse(
    val `data`: List<Data>,
    val success: Boolean,
    val message: String
)

data class Data(
    val businessaddress: String,
    val businesscategory: String,
    val businesscellphone: String,
    val businesscity: String,
    val businessname: String,
    val review_count: String,
    val businessphone: String,
    val businessregion: String,
    val businessspecializesin: String,
    val businessstar: String,
    val businessworeda: String,
    val created_at: String,
    val device_id: String,
    val id: Int,
    val latlng: String,
    val logo: String,
    val operatinghrs: String,
    val postalcode: String,
    val registrationnumebr: String,
    val updated_at: String
):Serializable