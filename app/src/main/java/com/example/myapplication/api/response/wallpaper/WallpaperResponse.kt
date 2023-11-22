package com.example.myapplication.api.response.wallpaper
 
data class WallpaperResponse(
    val `data`: List<Data>,
    val status: Int
)

data class Data(
    val category_id: String,
    val download_count: String,
    val id: Int,
    val isLiked: String,
    val like_count: String,
    val share_count: String,
    val tags: String,
    val title: String,
    val wallpaper: String
)