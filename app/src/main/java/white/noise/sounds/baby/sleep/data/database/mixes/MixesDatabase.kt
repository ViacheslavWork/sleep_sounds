package white.noise.sounds.baby.sleep.data.database.mixes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import white.noise.sounds.baby.sleep.data.database.entity.MixEntity
import white.noise.sounds.baby.sleep.data.database.entity.SoundEntity
import white.noise.sounds.baby.sleep.data.database.entity.SoundsEntitiesListConverter
import white.noise.sounds.baby.sleep.data.database.entity.UriConverters

@Database(
    entities = [MixEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(SoundsEntitiesListConverter::class, UriConverters::class)
abstract class MixesDatabase : RoomDatabase() {
    abstract val mixesDao: MixesDao
}