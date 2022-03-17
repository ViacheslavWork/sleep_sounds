package white.noise.sounds.baby.sleep.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import white.noise.sounds.baby.sleep.service.AlarmService
import white.noise.sounds.baby.sleep.data.database.entity.AlarmEntity
import white.noise.sounds.baby.sleep.service.RescheduleAlarmsService
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            startRescheduleAlarmsService(context)
        } else {
            startAlarmService(context, intent)
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
}