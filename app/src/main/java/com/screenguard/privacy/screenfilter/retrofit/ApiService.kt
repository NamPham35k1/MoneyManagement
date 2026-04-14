package com.screenguard.privacy.screenfilter.retrofit

import com.screenguard.privacy.screenfilter.model.Wallpaper
import retrofit2.http.GET

interface ApiService {
    @GET("api/APP7_WallpaperLauncher/all")
    suspend fun getAllWallpaper(): List<Wallpaper>
}