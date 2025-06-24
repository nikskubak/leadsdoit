package com.androsuperbooster.horoscope_feature.ui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.androsuperbooster.horoscope_feature.R
import com.androsuperbooster.horoscope_feature.domain.useCases.SelectedZodiacUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingServiceImpl :
    FirebaseMessagingService() {

    @Inject
    lateinit var selectedZodiacUseCase: SelectedZodiacUseCase

        companion object{
            const val TITLE = "title"
            const val SUB_TITLE = "subTitle"
            const val DETAILS = "details"
        }

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("RemoteMessage", remoteMessage.toString())
        showNotification(
            title = remoteMessage.notification?.title,
            message = remoteMessage.notification?.body,
            data = remoteMessage.data
        )
    }

    override fun onNewToken(token: String) {
        Log.e("onNewToken", token)
    }

    private fun showNotification(title: String?, message: String?, data: Map<String, String>) {
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
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            Log.e("push data", data.toString())
            this.data = Uri.parse(data[DETAILS])
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        selectedZodiacUseCase.getSelectedZodiac()
            .onEach { result ->
                result.getOrNull()?.let {
                    val notification = NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_daily) // Use your app's icon
                        .setLargeIcon(getBitmapFromVectorDrawable(it.animationRes ?: R.drawable.ic_taurus))
                        .setContentTitle(title ?: data[TITLE].toString())
                        .setContentText(message ?: data[SUB_TITLE].toString())
                        .setContentIntent(pendingIntent)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(message ?: data[SUB_TITLE].toString()))
                        .setAutoCancel(true)
                        .build()

                    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
                }
            }
            .launchIn(scope)
    }

    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}