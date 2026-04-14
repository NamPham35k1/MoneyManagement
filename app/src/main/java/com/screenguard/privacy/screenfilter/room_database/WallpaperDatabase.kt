package com.screenguard.privacy.screenfilter.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.screenguard.privacy.screenfilter.model.TypeWallpaperConverter
import com.screenguard.privacy.screenfilter.model.Wallpaper

@Database(entities = [Wallpaper::class], version = 1, exportSchema = false)
@TypeConverters(TypeWallpaperConverter::class)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun dao(): WallpaperDao
}