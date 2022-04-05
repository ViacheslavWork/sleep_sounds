package calm.sleep.relaxing.sounds.noise.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import calm.sleep.relaxing.sounds.noise.data.AlarmRepository
import calm.sleep.relaxing.sounds.noise.model.Alarm

class SettingsViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {
    fun setAlarm(alarm: Alarm) {
        viewModelScope.launch { alarmRepository.saveAlarm(alarm) }
    }

    suspend fun getAlarm(alarmId: Int): Alarm? {
        return alarmRepository.getAlarm(alarmId)
    }
}