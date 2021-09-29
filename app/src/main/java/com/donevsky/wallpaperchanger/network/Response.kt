package com.donevsky.wallpaperchanger.network

import com.squareup.moshi.Json

data class Response(
    val page: Int,
    @Json(name = "per_page")val perPage: Int,
    val photos: List<Wallpaper>,
    @Json(name = "next_page")val nextPage: String,
    @Json(name = "total_results")val totalResults: Int
) {
}

data class Wallpaper(
    val id: Long,
    val width: Int,
    val height: Int,
    val photographer: String,
    val photographer_id: Long,
    @Json(name = "src") val wallpaperUrls: WallpaperUrls,
    @Transient var isSelected: Boolean = false
) {
}

data class WallpaperUrls(
    val portrait: String
)
