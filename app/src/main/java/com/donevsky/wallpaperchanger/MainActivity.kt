package com.donevsky.wallpaperchanger

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
//import androidx.work.*
import com.donevsky.wallpaperchanger.database.getDatabase
import com.donevsky.wallpaperchanger.repository.WallpaperRepository
import com.donevsky.wallpaperchanger.work.ChangeWallpaperWorker
//import com.donevsky.wallpaperchanger.work.ChangeWallpaperWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch{
            setupRecurringWork()
            delay(15_000)
            val database = getDatabase(applicationContext)
            val repository = WallpaperRepository(database)
            var wallpapers = repository.getWallpapersFromDb()
            wallpapers = wallpapers.shuffled()
//            delay(15_000)
//            Log.i("wa", wallpapers.toString())
            Log.i("wa", wallpapers.toString())
            val url = URL(wallpapers[0].portraitUrl)
//            val url = URL("https://images.pexels.com/photos/167699/pexels-photo-167699.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800")
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val wallpaperManager = WallpaperManager.getInstance(applicationContext)
            try {
                wallpaperManager.setBitmap(bitmap)
            } catch (httpE : HttpException){

            }
        }
    }

    private fun setupRecurringWork() {
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresBatteryNotLow(true)
//            .apply {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    setRequiresDeviceIdle(true)
//                }
//            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<ChangeWallpaperWorker>(1, TimeUnit.MINUTES)
//            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            ChangeWallpaperWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}