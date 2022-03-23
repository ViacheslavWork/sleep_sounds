package relax.deep.sleep.sounds.calm.data.database.alarms

import androidx.room.Database
import androidx.room.RoomDatabase
import relax.deep.sleep.sounds.calm.data.database.entity.AlarmEntity

@Database(
    entities = [AlarmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlarmsDatabase : RoomDatabase() {
    abstract val alarmsDao: AlarmsDao
}