package calm.sleep.relaxing.sounds.noise.data.database.alarms

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import calm.sleep.relaxing.sounds.noise.data.database.entity.AlarmEntity

@Dao
interface AlarmsDao {
    @Query("SELECT * FROM alarms WHERE alarmId=:id")
    fun getAlarm(id: Int): AlarmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarmEntity: AlarmEntity)
}