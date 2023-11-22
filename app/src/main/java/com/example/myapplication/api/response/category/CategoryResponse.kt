package com.example.myapplication.api.response.category

data class CategoryResponse(
    val `data`: List<Data>,
    val status: Int
)

data class Data(
    val cover_photo: String,
    val id: Int,
    val name: String
)