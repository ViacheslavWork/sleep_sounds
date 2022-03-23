package relax.deep.sleep.sounds.calm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import relax.deep.sleep.sounds.calm.data.database.alarms.AlarmsDao
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity

class AlarmRepository(private val alarmsDao: AlarmsDao) {
    suspend fun getAlarm(id: Int): AlarmEntity? = withContext(Dispatchers.IO) {
        return@withContext alarmsDao.getAlarm(id)
    }

    suspend fun saveAlarm(alarmEntity: AlarmEntity) = withContext(Dispatchers.IO) {
        alarmsDao.insert(alarmEntity = alarmEntity)
    }
}