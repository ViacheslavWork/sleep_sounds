package calm.sleep.relaxing.sounds.noise.data.database.sounds

import androidx.room.Database
import androidx.room.RoomDatabase
import calm.sleep.relaxing.sounds.noise.data.database.entity.SoundEntity

@Database(
    entities = [SoundEntity::class],
    version = 5,
    exportSchema = false
)
abstract class SoundsDatabase : RoomDatabase() {
    abstract val soundsDao: SoundsDao
}