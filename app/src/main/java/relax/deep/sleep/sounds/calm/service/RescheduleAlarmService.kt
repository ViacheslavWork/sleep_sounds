package relax.deep.sleep.sounds.calm.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import relax.deep.sleep.sounds.calm.data.AlarmRepository
import relax.deep.sleep.sounds.calm.data.database.entity.toAlarm
import relax.deep.sleep.sounds.calm.utils.Constants
import relax.deep.sleep.sounds.calm.utils.MyLog.showLog

private const val TAG = "RescheduleAlarmService"
class RescheduleAlarmsService : LifecycleService() {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val alarmRepository: AlarmRepository by inject()
    private val context: Context by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        ioScope.launch {
            val alarm = async { alarmRepository.getAlarm(Constants.CUSTOM_ALARM_ID) }
            alarm.await()?.schedule(context)
            showLog("alarm rescheduled ", TAG)
        }

//        ioScope.launch { alarmRepository.getAlarm(Constants.CUSTOM_ALARM_ID)?.schedule(context) }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}
