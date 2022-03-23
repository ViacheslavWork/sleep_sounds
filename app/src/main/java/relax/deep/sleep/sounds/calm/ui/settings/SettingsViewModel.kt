package relax.deep.sleep.sounds.calm.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import relax.deep.sleep.sounds.calm.data.AlarmRepository
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity

class SettingsViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {

    fun setAlarm(alarmEntity: AlarmEntity) {
        viewModelScope.launch { alarmRepository.saveAlarm(alarmEntity) }
    }

    suspend fun getAlarm(alarmId: Int): AlarmEntity? {
        return alarmRepository.getAlarm(alarmId)
    }
}