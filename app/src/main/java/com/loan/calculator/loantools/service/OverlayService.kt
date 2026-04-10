package com.loan.calculator.loantools.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ui.defaut_ui.splash.SplashActivity
import com.loan.calculator.loantools.ui.feature.home.HomeActivity

class OverlayService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundService() {

        val channelId = "overlay_channel"

        // tạo notification channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Overlay Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // mở lại app khi click
        val intent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Loan Tools đang chạy")
            .setContentText("Bấm để mở lại app và tắt")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // không swipe tắt
            .build()

        startForeground(1, notification)
    }
}