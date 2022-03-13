package white.noise.sounds.baby.sleep.data.database.alarms

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import white.noise.sounds.baby.sleep.data.database.entity.MixEntity
import white.noise.sounds.baby.sleep.data.database.entity.AlarmEntity

@Dao
interface AlarmsDao {
    @Query("SELECT * FROM alarms WHERE alarmId=:id")
    fun getAlarm(id: Int): AlarmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarmEntity: AlarmEntity)
}