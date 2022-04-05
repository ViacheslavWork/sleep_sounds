package calm.sleep.relaxing.sounds.noise.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import calm.sleep.relaxing.sounds.noise.data.AlarmRepository
import calm.sleep.relaxing.sounds.noise.utils.Constants
import calm.sleep.relaxing.sounds.noise.utils.EveryDayAlarmManager

private const val TAG = "RescheduleAlarmService"

class RescheduleAlarmsService : LifecycleService() {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val alarmRepository: AlarmRepository by inject()
    private val context: Context by inject()
    private val everyDayAlarmManager: EveryDayAlarmManager by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        ioScope.launch {
            val alarm = async { alarmRepository.getAlarm(Constants.CUSTOM_ALARM_ID) }
            if (alarm.await()?.started == true) {
                alarm.await()?.schedule(context)
            } else {
                everyDayAlarmManager.scheduleEveryDayAlarm()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}
