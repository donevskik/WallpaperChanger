package com.donevsky.wallpaperchanger.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseWallpaperModel constructor(
    @PrimaryKey
    val id: Long,
    val portraitUrl: String
)