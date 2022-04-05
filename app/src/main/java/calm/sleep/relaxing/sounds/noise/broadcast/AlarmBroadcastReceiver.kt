package calm.sleep.relaxing.sounds.noise.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import calm.sleep.relaxing.sounds.noise.MainActivity
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.model.Alarm
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.FRIDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.HOUR
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.IS_CUSTOM
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.MINUTE
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.MONDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.SATURDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.SUNDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.THURSDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.TITLE
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.TUESDAY
import calm.sleep.relaxing.sounds.noise.model.Alarm.Companion.WEDNESDAY
import calm.sleep.relaxing.sounds.noise.service.AlarmService
import calm.sleep.relaxing.sounds.noise.service.RescheduleAlarmsService
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.Constants.ALARM_ID
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        } else {
            sendNotification(
                context,
                intent.getStringExtra(TITLE),
                intent.getBooleanExtra(IS_CUSTOM, false)
            )
            startNextAlarm(context, intent)
        }
    }

    private fun startNextAlarm(context: Context, intent: Intent) {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val alarm = Alarm(
            alarmId = intent.getIntExtra(ALARM_ID, Constants.EVERY_DAY_ALARM_ID),
            hour = intent.getIntExtra(HOUR, calendar.get(Calendar.HOUR_OF_DAY)),
            minute = intent.getIntExtra(MINUTE, calendar.get(Calendar.MINUTE)),
            title = intent.getStringExtra(TITLE)
                ?: context.getString(R.string.custom_alarm_notification_title),
            started = true,
            isCustom = intent.getBooleanExtra(IS_CUSTOM, false),
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


    private fun sendNotification(context: Context, stringExtra: String?, isCustomAlarm: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder(context)

        val action = if (!isCustomAlarm) MainActivity.ACTION_SHOW_PLAYER else null

        notificationBuilder.setContentTitle(stringExtra)
            .setContentIntent(getMainActivityPendingIntent(context, action))
        notificationManager.notify(
            Constants.BEDTIME_REMINDER_NOTIFICATION_ID,
            notificationBuilder.build()
        )
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


    private fun getMainActivityPendingIntent(context: Context, action: String? = null) =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                this.action = action
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
}