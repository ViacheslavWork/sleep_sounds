package relax.deep.sleep.sounds.calm.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import relax.deep.sleep.sounds.calm.MainActivity
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity
import relax.deep.sleep.sounds.calm.service.AlarmService
import relax.deep.sleep.sounds.calm.service.RescheduleAlarmsService
import relax.deep.sleep.sounds.calm.utils.Constants
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        } else {
            sendNotification(context)
            startNextAlarm(context, intent)
        }
    }

    private fun startNextAlarm(context: Context, intent: Intent) {
        val alarmId: Int = 1
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val alarm = AlarmEntity(
            alarmId = alarmId,
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            title = intent.getStringExtra(TITLE) ?: "It is time to sleep",
            started = true,
            monday = intent.getBooleanExtra(MONDAY, false),
            tuesday = intent.getBooleanExtra(TUESDAY, false),
            wednesday = intent.getBooleanExtra(WEDNESDAY, false),
            thursday = intent.getBooleanExtra(THURSDAY, false),
            friday = intent.getBooleanExtra(FRIDAY, false),
            saturday = intent.getBooleanExtra(SATURDAY, false),
            sunday = intent.getBooleanExtra(SUNDAY, false)
        )
        alarm.schedule(context)
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
    }

    private fun sendNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder(context)
        notificationManager.notify(Constants.BEDTIME_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
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

    private fun getNotificationBuilder(context: Context) = NotificationCompat.Builder(
        context,
        Constants.NOTIFICATION_CHANNEL_ID
    )
        .setAutoCancel(true)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_moon)
        .setContentTitle("It is time to sleep!")
        .setColorized(true)
        .setColor(context.resources.getColor(R.color.dark_blue, null))
        .setContentIntent(getMainActivityPendingIntent(context))

    private fun getMainActivityPendingIntent(context: Context) = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}