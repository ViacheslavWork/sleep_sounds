package white.noise.sounds.baby.sleep.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import white.noise.sounds.baby.sleep.MainActivity
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.ui.timer.Times
import white.noise.sounds.baby.sleep.utils.Constants
import white.noise.sounds.baby.sleep.utils.Constants.ACTION_PAUSE_SERVICE
import white.noise.sounds.baby.sleep.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import white.noise.sounds.baby.sleep.utils.Constants.ACTION_STOP_SERVICE
import white.noise.sounds.baby.sleep.utils.Constants.NOTIFICATION_CHANNEL_ID
import white.noise.sounds.baby.sleep.utils.Constants.NOTIFICATION_CHANNEL_NAME
import white.noise.sounds.baby.sleep.utils.Constants.NOTIFICATION_ID

private const val TAG = "TimerService"

class TimerService : LifecycleService() {
    lateinit var currentTimer: Job
    private var isFirstRun = true

    companion object {
        private val _isTimerRunning = MutableLiveData<Boolean>()
        val isTimerRunning: LiveData<Boolean> = _isTimerRunning

        private val _timerTime = MutableLiveData<LocalTime>()
        val timerTime: LiveData<LocalTime> = _timerTime
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Log.i(
                        TAG,
                        "Started or resumed service: time - ${it.extras?.get(Constants.EXTRA_TIME)}"
                    )
                    if (::currentTimer.isInitialized) {
                        currentTimer.cancel()
                    }

                    val time = (it.extras?.get(Constants.EXTRA_TIME) as Times).time
                    if (time != null) {
                        startTimer(time)
                        _isTimerRunning.postValue(true)
                    } else {
                        _isTimerRunning.postValue(false)
                    }

                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    }
                }
                ACTION_PAUSE_SERVICE -> {

                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
                else -> Unit
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = getNotificationBuilder()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        timerTime.observe(this) {
            val notification = getNotificationBuilder()
                .setContentText(it.toString())
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
        isTimerRunning.observe(this) {
            if (!it) {
                stopService()
            }
        }
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun getNotificationBuilder() = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_dynamic)
        .setContentTitle("White noise")
        .setContentText("00:00:00")
        .setContentIntent(getMainActivityPendingIntent())

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java)
        /*.also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        }*/, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startTimer(time: LocalTime) {
        currentTimer = tickerFlow(time, Duration.ofSeconds(1))
            .onEach {
                Log.i(TAG, "startTimer: $it")
                _timerTime.postValue(it)
                if (it == LocalTime.of(0, 0, 0)) {
                    _isTimerRunning.postValue(false)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun tickerFlow(duration: LocalTime, period: Duration) = flow {
        var localRestOfTime = duration
        while (localRestOfTime.isAfter(LocalTime.of(0, 0, 0))) {
            localRestOfTime = localRestOfTime.minusSeconds(1)
            emit(localRestOfTime)
            delay(period.toMillis())
        }
    }

}