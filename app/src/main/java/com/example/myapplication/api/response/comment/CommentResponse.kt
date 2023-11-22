package com.example.myapplication.api.response.comment

data class CommentResponse(
    val `data`: List<Data>,
    val success: Boolean
)

data class Data(
    val comment: String,
    val created_at: String,
    val id: Int,
    val name: String,
    val phone_no: Long,
    val post_id: Int,
    val updated_at: String,
    val user_id: Int,
    val user_image: Any
)