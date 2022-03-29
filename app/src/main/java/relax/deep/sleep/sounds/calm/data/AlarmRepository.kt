package relax.deep.sleep.sounds.calm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import relax.deep.sleep.sounds.calm.data.database.alarms.AlarmsDao
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity
import relax.deep.sleep.sounds.calm.data.database.entity.toAlarm
import relax.deep.sleep.sounds.calm.model.Alarm

class AlarmRepository(private val alarmsDao: AlarmsDao) {
    suspend fun getAlarm(id: Int): Alarm? = withContext(Dispatchers.IO) {
        return@withContext alarmsDao.getAlarm(id)?.toAlarm()
    }

    suspend fun saveAlarm(alarm: Alarm) = withContext(Dispatchers.IO) {
        alarmsDao.insert(alarmEntity = AlarmEntity.fromAlarm(alarm))
    }
}