package relax.deep.sleep.sounds.calm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.broadcast.AlarmBroadcastReceiver
import relax.deep.sleep.sounds.calm.data.AlarmRepository
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity
import relax.deep.sleep.sounds.calm.model.Alarm
import relax.deep.sleep.sounds.calm.utils.Constants.CUSTOM_ALARM_ID

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