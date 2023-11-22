package com.example.myapplication.api.response.post

import org.w3c.dom.Comment
import java.io.Serializable

data class PostResponse(
    val `data`: List<Data>,
    val success: Boolean
)

data class Data(
    val created_at: String,
    val description: String,
    val favorites_count: Int,
    val id: Int,
    val name: String,
    val phone_no: Long,
    val post_image: String,
    val post_image_name: String,
    val post_title: String,
    val post_type: String,
    val price: Int,
    val review_count: Int,
    val comment_count: Int,
    val updated_at: String,
    val user_image: String,
    val is_liked: String,
    val user_id: Int
):Serializable