package relax.deep.sleep.sounds.calm.service

import android.content.Intent
import android.util.Log
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
import relax.deep.sleep.sounds.calm.ui.timer.Times
import relax.deep.sleep.sounds.calm.utils.Constants

private const val TAG = "TimerService"

class TimerService : LifecycleService() {
    companion object {
        private val _isTimerStarted = MutableLiveData<Boolean>()
        val isTimerStarted: LiveData<Boolean> = _isTimerStarted

        private val _timerTime = MutableLiveData<LocalTime>()
        val timerTime: LiveData<LocalTime> = _timerTime
    }

    private lateinit var currentTimer: Job

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: ")
        intent?.let {
            if (it.action == Constants.ACTION_START_TIMER) {
                if (::currentTimer.isInitialized) {
                    currentTimer.cancel()
                }
                if (it.extras != null) {
                    val time = (it.extras?.get(Constants.EXTRA_TIME) as Times).time
                    if (time != null) {
                        _isTimerStarted.postValue(true)
                        startTimer(time)
                    }else {
                        //this means off clicked in time selected dialog
                        _isTimerStarted.postValue(false)
                        _timerTime.postValue(LocalTime.of(0,0,0))
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(time: LocalTime) {
        currentTimer = tickerFlow(time, Duration.ofSeconds(1))
            .onEach {
                _timerTime.postValue(it)
                if (it == LocalTime.of(0, 0, 0)) {
                    _isTimerStarted.postValue(false)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun tickerFlow(duration: LocalTime, period: Duration) = flow {
        var localRestOfTime = duration
        do {
            localRestOfTime = localRestOfTime.minusSeconds(1)
            emit(localRestOfTime)
            delay(period.toMillis())
        } while (localRestOfTime.isAfter(LocalTime.of(0, 0, 0)))
    }
}