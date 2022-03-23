package relax.deep.sleep.sounds.calm.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "TimerViewModel"

class TimerViewModel : ViewModel() {
    private val _selectedTime = MutableLiveData<Times>(Times.off)
    val selectedTime: LiveData<Times> = _selectedTime

    fun setTime(time: Times) {
        _selectedTime.postValue(time)
    }
}