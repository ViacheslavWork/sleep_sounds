package relax.deep.sleep.sounds.calm.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import relax.deep.sleep.sounds.calm.data.AlarmRepository
import relax.deep.sleep.sounds.calm.model.Alarm

class SettingsViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {
    fun setAlarm(alarm: Alarm) {
        viewModelScope.launch { alarmRepository.saveAlarm(alarm) }
    }

    suspend fun getAlarm(alarmId: Int): Alarm? {
        return alarmRepository.getAlarm(alarmId)
    }
}