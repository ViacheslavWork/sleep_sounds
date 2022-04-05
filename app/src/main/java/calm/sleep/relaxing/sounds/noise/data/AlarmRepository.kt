package calm.sleep.relaxing.sounds.noise.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import calm.sleep.relaxing.sounds.noise.data.database.alarms.AlarmsDao
import calm.sleep.relaxing.sounds.noise.data.database.entity.AlarmEntity
import calm.sleep.relaxing.sounds.noise.data.database.entity.toAlarm
import calm.sleep.relaxing.sounds.noise.model.Alarm

class AlarmRepository(private val alarmsDao: AlarmsDao) {
    suspend fun getAlarm(id: Int): Alarm? = withContext(Dispatchers.IO) {
        return@withContext alarmsDao.getAlarm(id)?.toAlarm()
    }

    suspend fun saveAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmsDao.insert(alarmEntity = AlarmEntity.fromAlarm(alarm))
    }
}