package relax.deep.sleep.sounds.calm.data.database.sounds

import androidx.room.Database
import androidx.room.RoomDatabase
import relax.deep.sleep.sounds.calm.data.database.entity.SoundEntity

@Database(
    entities = [SoundEntity::class],
    version = 4,
    exportSchema = false
)
abstract class SoundsDatabase : RoomDatabase() {
    abstract val soundsDao: SoundsDao
}