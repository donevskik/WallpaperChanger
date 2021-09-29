package com.donevsky.wallpaperchanger.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.donevsky.wallpaperchanger.database.DatabaseWallpaperModel
import com.donevsky.wallpaperchanger.database.WallpaperDatabase
import com.donevsky.wallpaperchanger.network.PexelsApi
import com.donevsky.wallpaperchanger.network.Wallpaper
import com.donevsky.wallpaperchanger.wallpaperoverview.PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WallpaperRepository(private val database: WallpaperDatabase) {

    private val _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers: LiveData<List<Wallpaper>>
        get() = _wallpapers

    suspend fun getWallpapersFromApi(query: String){
        try{
            val result = PexelsApi.retrofitService.getWallpapersApi(query, PER_PAGE)

            if (result.photos.size > 0){
                _wallpapers.value = result.photos
            }
        } catch (error: Throwable){
            _wallpapers.value = ArrayList()
        }
    }

    suspend fun addFavesToDatabase(faves: List<Wallpaper>) {
        withContext(Dispatchers.IO){
            val dbmodel = faves.asDatabaseModel()
            Log.i("dbmodel", dbmodel.toString())
            database.wallpaperDao.addWallpapers(*dbmodel)
        }
    }

    suspend fun getWallpapersFromDb(): List<DatabaseWallpaperModel> {
        return database.wallpaperDao.getWallpapers()
    }
}


private fun List<Wallpaper>.asDatabaseModel(): Array<DatabaseWallpaperModel> {
    return this.map {
        DatabaseWallpaperModel(
            id = it.id,
            portraitUrl = it.wallpaperUrls.portrait
        )
    }.toTypedArray()
}
