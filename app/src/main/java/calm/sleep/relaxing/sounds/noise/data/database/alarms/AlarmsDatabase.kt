package calm.sleep.relaxing.sounds.noise.data.database.alarms

import androidx.room.Database
import androidx.room.RoomDatabase
import calm.sleep.relaxing.sounds.noise.data.database.entity.AlarmEntity

@Database(
    entities = [AlarmEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AlarmsDatabase : RoomDatabase() {
    abstract val alarmsDao: AlarmsDao
}