package com.donevsky.wallpaperchanger.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WallpaperDao {
    @Query("select * from databasewallpapermodel")
    fun getWallpapers(): List<DatabaseWallpaperModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWallpapers(vararg wallpapers: DatabaseWallpaperModel)

}

@Database(entities = [DatabaseWallpaperModel::class], version = 2)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract val wallpaperDao: WallpaperDao
}

private lateinit var INSTANCE: WallpaperDatabase

fun getDatabase(context: Context): WallpaperDatabase {
    synchronized(WallpaperDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                WallpaperDatabase::class.java,
                "videos").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}