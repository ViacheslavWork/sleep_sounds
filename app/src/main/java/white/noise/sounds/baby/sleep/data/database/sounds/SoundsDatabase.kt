package white.noise.sounds.baby.sleep.data.database.sounds

import androidx.room.Database
import androidx.room.RoomDatabase
import white.noise.sounds.baby.sleep.data.database.entity.SoundEntity

@Database(
    entities = [SoundEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SoundsDatabase : RoomDatabase() {
    abstract val soundsDao: SoundsDao
}