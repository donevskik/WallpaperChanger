package com.donevsky.wallpaperchanger.wallpaperoverview

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.donevsky.wallpaperchanger.database.getDatabase
import com.donevsky.wallpaperchanger.repository.WallpaperRepository
import com.donevsky.wallpaperchanger.network.Wallpaper
import kotlinx.coroutines.launch

val PER_PAGE = 50

class WallpaperOverviewViewModel(application: Application) : ViewModel() {

    val database = getDatabase(application)
    val wallpaperRepository = WallpaperRepository(database)

    val wallpapers = wallpaperRepository.wallpapers

    init {
        getWallpapers("landscape")
    }

    fun addFavourites(faves: List<Wallpaper>){
        Log.i("List", faves.toString())
        viewModelScope.launch{
            wallpaperRepository.addFavesToDatabase(faves)
        }
    }

    fun getWallpapers(query: String) {
        viewModelScope.launch {
            wallpaperRepository.getWallpapersFromApi(query)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WallpaperOverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WallpaperOverviewViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}