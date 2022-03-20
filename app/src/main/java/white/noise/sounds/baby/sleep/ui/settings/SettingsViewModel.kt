package white.noise.sounds.baby.sleep.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import white.noise.sounds.baby.sleep.data.AlarmRepository
import white.noise.sounds.baby.sleep.data.database.entity.AlarmEntity

class SettingsViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {

    fun setAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch { alarmRepository.saveAlarm(alarmEntity) }
    }

    suspend fun getAlarm(alarmId: Int): AlarmEntity? {
        return alarmRepository.getAlarm(alarmId)
    }
}