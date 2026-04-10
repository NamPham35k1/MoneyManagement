package com.loan.calculator.loantools.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loan.calculator.loantools.model.TypeWallpaperConverter
import com.loan.calculator.loantools.model.Wallpaper

@Database(entities = [Wallpaper::class], version = 1, exportSchema = false)
@TypeConverters(TypeWallpaperConverter::class)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun dao(): WallpaperDao
}