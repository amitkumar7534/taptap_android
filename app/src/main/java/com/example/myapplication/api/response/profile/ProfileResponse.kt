package com.example.myapplication.api.response.profile

data class ProfileResponse(
    val `data`: Data,
    val message: String,
    val post_count: Int,
    val product_count: Int,
    val posts: List<com.example.myapplication.api.response.post.Data>,
    val success: Boolean
)

data class Data(
    val _id: Int,
    val device_id: String,
    val device_token: String,
    val full_name: String,
    val private_mode: String,
    val phone_no: Long,
    val profile_pic: String
)

data class Post(
    val created_at: String,
    val description: String,
    val favorites_count: Int,
    val id: Int,
    val post_image: String,
    val post_image_name: String,
    val post_title: String,
    val post_type: String,
    val price: Int,
    val review_count: Int,
    val updated_at: String,
    val user_id: Int
)