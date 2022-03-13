package white.noise.sounds.baby.sleep.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import white.noise.sounds.baby.sleep.data.database.alarms.AlarmsDao
import white.noise.sounds.baby.sleep.data.database.entity.AlarmEntity

class AlarmRepository(private val alarmsDao: AlarmsDao) {
    suspend fun getAlarm(id: Int): AlarmEntity? = withContext(Dispatchers.IO) {
        return@withContext alarmsDao.getAlarm(id)
    }

    suspend fun saveAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.insert(alarmEntity = alarmEntity)
    }
}