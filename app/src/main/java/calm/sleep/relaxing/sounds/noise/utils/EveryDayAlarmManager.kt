package calm.sleep.relaxing.sounds.noise.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.broadcast.AlarmBroadcastReceiver
import calm.sleep.relaxing.sounds.noise.data.AlarmRepository
import calm.sleep.relaxing.sounds.noise.model.Alarm
import calm.sleep.relaxing.sounds.noise.utils.Constants.CUSTOM_ALARM_ID

class EveryDayAlarmManager(val context: Context, val alarmRepository: AlarmRepository) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    fun startStopEveryDayAlarmIfNeeded() {
        ioScope.launch {
            val customAlarm = async { alarmRepository.getAlarm(CUSTOM_ALARM_ID) }
            if (customAlarm.await()?.started == true) {
                cancelEveryDayAlarm()
            } else {
                scheduleEveryDayAlarm()
            }
        }
    }

    fun scheduleEveryDayAlarm() {
        val alarm = Alarm(
            alarmId = Constants.EVERY_DAY_ALARM_ID,
            hour = Constants.EVERY_DAY_REMINDER_TIME.hour,
            minute = Constants.EVERY_DAY_REMINDER_TIME.minute,
            title = context.resources.getString(R.string.default_alarm_notification_title),
            started = true,
            isCustom = false,
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )
        alarm.schedule(context)
    }

    fun scheduleCustomEveryDayAlarm(
        hour: Int,
        minute: Int,
        alarmId: Int,
        notificationTitle: String
    ) {
        val alarm = Alarm(
            alarmId = alarmId,
            hour = hour,
            minute = minute,
            title = notificationTitle,
            started = true,
            isCustom = false,
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = true
        )
        ioScope.launch { alarmRepository.saveAlarm(alarm) }
        alarm.schedule(context)
    }

    fun cancelEveryDayAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.EVERY_DAY_ALARM_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(alarmPendingIntent)
    }
}