package white.noise.sounds.baby.sleep.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.data.AlarmRepository

class RescheduleAlarmsService(
    private val alarmRepository: AlarmRepository,
    private val context: Context
) : LifecycleService() {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        ioScope.launch { alarmRepository.getAlarm(1)?.schedule(context) }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}
