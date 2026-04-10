package com.loan.calculator.loantools.retrofit

import com.loan.calculator.loantools.model.Wallpaper
import retrofit2.http.GET

interface ApiService {
    @GET("api/APP7_WallpaperLauncher/all")
    suspend fun getAllWallpaper(): List<Wallpaper>
}