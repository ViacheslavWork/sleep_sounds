package calm.sleep.relaxing.sounds.noise.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import calm.sleep.relaxing.sounds.noise.MainActivity
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog


private const val TAG = "MyFirebaseMessagingServ"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            showLog("onMessageReceived: data isn't empty", TAG)
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            showNotification(applicationContext, title, body, null)
        } else {
            showLog("onMessageReceived: data is empty", TAG)
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            val image = remoteMessage.notification!!.imageUrl
            showNotification(applicationContext, title, body, imageUrl = image)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        showLog("onNewToken: $token", TAG)
    }


    private fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        imageUrl: Uri?
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder()
        notificationBuilder
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        if (imageUrl != null) {
            setLargeIconToNotification(imageUrl, notificationBuilder, notificationManager)
        } else {
            notify(notificationManager, notificationBuilder)
        }
    }

    private fun setLargeIconToNotification(
        imageUrl: Uri?,
        notificationBuilder: NotificationCompat.Builder,
        notificationManager: NotificationManager
    ) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    notificationBuilder.setLargeIcon(resource)
                    notify(notificationManager, notificationBuilder)
                }

                override fun onLoadCleared(placeholder: Drawable?) { }
            })
    }

    private fun notify(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder
    ) {
        notificationManager.notify(
            Constants.FIREBASE_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun getNotificationBuilder() = NotificationCompat.Builder(
        this,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(true)
        .setOngoing(false)
        .setCategory(Notification.EXTRA_MEDIA_SESSION)
        .setSmallIcon(R.drawable.ic_moon)
        .setColor(resources.getColor(R.color.dark_blue, null))
        .setColorized(true)
        .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
        .setContentIntent(getMainActivityPendingIntent())

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}