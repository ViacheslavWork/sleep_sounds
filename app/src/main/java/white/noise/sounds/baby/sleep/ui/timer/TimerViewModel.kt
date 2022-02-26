package white.noise.sounds.baby.sleep.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import white.noise.sounds.baby.sleep.model.Sound
import java.util.*

class TimerViewModel: ViewModel() {
    private val _selectedTime = MutableLiveData<Date>()
    val selectedTime: LiveData<Date> = _selectedTime

    fun setTime(date: Date) {
        _selectedTime.postValue(date)
    }
}