package com.donevsky.wallpaperchanger.work

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.donevsky.wallpaperchanger.database.getDatabase
import com.donevsky.wallpaperchanger.repository.WallpaperRepository
import retrofit2.HttpException
import java.net.HttpRetryException
import java.net.URL

class ChangeWallpaperWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "ChangeWallpaperWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = WallpaperRepository(database)
        var wallpapers = repository.getWallpapersFromDb()
        wallpapers = wallpapers.shuffled()
        Log.i("worker", wallpapers.toString())
        val url = URL(wallpapers[0].portraitUrl)
        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        return try {
            wallpaperManager.setBitmap(bitmap)
            Result.success()
        } catch (httpE : HttpException){
            Result.retry()
        }
    }

}