package white.noise.sounds.baby.sleep.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.threeten.bp.LocalTime

private const val TAG = "TimerViewModel"

class TimerViewModel() : ViewModel() {
    private val _selectedTime = MutableLiveData<Times>(Times.off)
    val selectedTime: LiveData<Times> = _selectedTime

    lateinit var restOfTime: LocalTime
    lateinit var job: Job

    init {
        /*startTimer(LocalTime.of(0, 0, 20))
        startTimer(LocalTime.of(0, 0, 10))
        GlobalScope.launch {
            delay(5000)
            job.cancel()
        }*/
    }

    fun setTime(time: Times) {
        _selectedTime.postValue(time)
    }


}