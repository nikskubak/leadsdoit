package com.androsuperbooster.horoscope.ui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.androsuperbooster.horoscope.R
import com.androsuperbooster.horoscope.ui.base.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("RemoteMessage", remoteMessage.toString())
        // 1. Handle notification payload (visible to user)
        remoteMessage.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Notification",
                message = notification.body ?: "",
                data = remoteMessage.data
            )
        }

        // 2. Handle data payload (technical push, not shown to user)
        if (remoteMessage.notification == null && remoteMessage.data.isNotEmpty()) {
            handleTechnicalPush(remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        Log.e("onNewToken",token)
    }

    private fun showNotification(title: String, message: String, data: Map<String, String>) {
        val channelId = "default_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open MainActivity (customize as needed)
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // Optionally pass data to activity
            data.forEach { (key, value) -> putExtra(key, value) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_daily) // Use your app's icon
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun handleTechnicalPush(data: Map<String, String>) {
        // Handle your technical push here (e.g., update config, sync data, etc.)
        // Example: Log or trigger a background task
        // Log.d("FCM", "Received technical push: $data")
    }
}