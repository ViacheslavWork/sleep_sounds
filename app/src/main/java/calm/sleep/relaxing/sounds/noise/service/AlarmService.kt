package calm.sleep.relaxing.sounds.noise.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import calm.sleep.relaxing.sounds.noise.MainActivity
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.TITLE
import calm.sleep.relaxing.sounds.noise.utils.Constants


class AlarmService : Service() {
    private var title: String = ""
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        /*val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE))
        //TODO
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_dynamic)
                .setContentIntent(pendingIntent)
                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        startForeground(1, notification)*/
        intent.getStringExtra(TITLE).let { title = intent.getStringExtra(TITLE)!! }
        startForegroundService()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder()
        startForeground(Constants.PLAYER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationBuilder() = NotificationCompat.Builder(
        this,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(true)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_moon)
        .setContentTitle("It is time to sleep!")
        .setColorized(true)
        .setColor(resources.getColor(R.color.dark_blue, null))
        .setContentIntent(getMainActivityPendingIntent())

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}