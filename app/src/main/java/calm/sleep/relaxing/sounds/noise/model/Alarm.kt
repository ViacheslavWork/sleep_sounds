package calm.sleep.relaxing.sounds.noise.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import calm.sleep.relaxing.sounds.noise.broadcast.AlarmBroadcastReceiver
import calm.sleep.relaxing.sounds.noise.utils.Constants.ALARM_ID
import java.util.*

@Parcelize
data class Alarm(
    val alarmId: Int,
    val hour: Int,
    val minute: Int,
    val title: String,
    var started: Boolean,
    val isCustom: Boolean,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
) : Parcelable {
    companion object {
        const val HOUR = "HOUR"
        const val MINUTE = "MINUTE"
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
        const val IS_CUSTOM = "CUSTOM"
    }

    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(HOUR, hour)
        intent.putExtra(MINUTE, minute)
        intent.putExtra(MONDAY, monday)
        intent.putExtra(TUESDAY, tuesday)
        intent.putExtra(WEDNESDAY, wednesday)
        intent.putExtra(THURSDAY, thursday)
        intent.putExtra(FRIDAY, friday)
        intent.putExtra(SATURDAY, saturday)
        intent.putExtra(SUNDAY, sunday)
        intent.putExtra(TITLE, title)
        intent.putExtra(IS_CUSTOM, isCustom)
        intent.putExtra(ALARM_ID, alarmId)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        var isCurrentDayNotAlarm = true
        val alarmDays = getAlarmDays()
        while (isCurrentDayNotAlarm) {
            if (alarmDays.contains(calendar.get(Calendar.DAY_OF_WEEK))
                && calendar.timeInMillis > System.currentTimeMillis()
            ) {
                isCurrentDayNotAlarm = false
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
            }
        }

        /*var toastText: String? = null
        try {
            toastText = java.lang.String.format(
                "One Time Alarm %s scheduled for %s at %02d:%02d",
                title, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)),
                hour,
                minute,
                alarmId
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()*/

        val alarmClockInfo =
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, alarmPendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, alarmPendingIntent)
        started = true
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(alarmPendingIntent)
        started = false
    }

    private fun getAlarmDays(): Set<Int> {
        val alarmDays = mutableSetOf<Int>()
        if (monday) alarmDays.add(Calendar.MONDAY)
        if (tuesday) alarmDays.add(Calendar.TUESDAY)
        if (wednesday) alarmDays.add(Calendar.WEDNESDAY)
        if (thursday) alarmDays.add(Calendar.THURSDAY)
        if (friday) alarmDays.add(Calendar.FRIDAY)
        if (saturday) alarmDays.add(Calendar.SATURDAY)
        if (sunday) alarmDays.add(Calendar.SUNDAY)
        return alarmDays
    }
}