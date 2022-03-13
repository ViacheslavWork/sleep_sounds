package white.noise.sounds.baby.sleep.ui.settings.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.room.PrimaryKey
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.FRIDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.MONDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.RECURRING
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.SATURDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.SUNDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.THURSDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.TITLE
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.TUESDAY
import white.noise.sounds.baby.sleep.broadcast.AlarmBroadcastReceiver.Companion.WEDNESDAY
import white.noise.sounds.baby.sleep.utils.DayUtil
import java.util.*


class Alarm(
    @field:PrimaryKey private val alarmId: Int,
    private val hour: Int,
    private val minute: Int,
    private val title: String,
    private var started: Boolean,
    private val recurring: Boolean,
    private val monday: Boolean,
    private val tuesday: Boolean,
    private val wednesday: Boolean,
    private val thursday: Boolean,
    private val friday: Boolean,
    private val saturday: Boolean,
    private val sunday: Boolean
) {
    @SuppressLint("DefaultLocale")
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(RECURRING, recurring)
        intent.putExtra(MONDAY, monday)
        intent.putExtra(TUESDAY, tuesday)
        intent.putExtra(WEDNESDAY, wednesday)
        intent.putExtra(THURSDAY, thursday)
        intent.putExtra(FRIDAY, friday)
        intent.putExtra(SATURDAY, saturday)
        intent.putExtra(SUNDAY, sunday)
        intent.putExtra(TITLE, title)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }
        if (!recurring) {
            var toastText: String? = null
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
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            /*alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                alarmPendingIntent
            )*/
            val alarmClockInfo =
                AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
            alarmManager.setAlarmClock(alarmClockInfo, alarmPendingIntent)
        } else {
            val toastText = java.lang.String.format(
                "Recurring Alarm %s scheduled for %s at %02d:%02d",
                title, getRecurringDaysText(),
                hour,
                minute,
                alarmId
            )
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                RUN_DAILY,
                alarmPendingIntent
            )
        }
        started = true
    }

    private fun getRecurringDaysText(): String? {
        if (!recurring) {
            return null
        }
        var days = ""
        if (monday) {
            days += "Mo "
        }
        if (tuesday) {
            days += "Tu "
        }
        if (wednesday) {
            days += "We "
        }
        if (thursday) {
            days += "Th "
        }
        if (friday) {
            days += "Fr "
        }
        if (saturday) {
            days += "Sa "
        }
        if (sunday) {
            days += "Su "
        }
        return days
    }
}